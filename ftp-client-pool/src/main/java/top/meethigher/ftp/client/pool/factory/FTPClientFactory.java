package top.meethigher.ftp.client.pool.factory;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.meethigher.ftp.client.pool.config.FTPPoolConfig;
import top.meethigher.ftp.client.pool.utils.Slf4jPrintCommandListener;

import java.time.Duration;


/**
 * FTPClient工厂
 *
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @see <a href="https://github.com/XiaZengming/FtpClientPool">XiaZengming/FtpClientPool</a>
 * @since 2023/10/21 03:01
 */
public class FTPClientFactory extends BasePooledObjectFactory<FTPClient> {

    protected static final Logger log = LoggerFactory.getLogger(FTPClientFactory.class);

    protected final FTPPoolConfig poolConfig;

    public FTPClientFactory(FTPPoolConfig config) {
        this.poolConfig = config;
    }

    public FTPPoolConfig getPoolConfig() {
        return poolConfig;
    }

    /**
     * 创建client比较耗时，在并发时，需要保证client的创建支持原子性，否则会出现大量连接被创建，进而导致池连接出现问题
     */
    @Override
    public synchronized FTPClient create() throws Exception {
        long startMills = System.currentTimeMillis();
        FTPClient ftpClient = new FTPClient();
        if (getPoolConfig().isDebug()) {
            //ftpClient.addProtocolCommandListener(new org.apache.commons.net.PrintCommandListener(System.out));
            // 使用自己封装的日志Slf4j实现
            ftpClient.addProtocolCommandListener(new Slf4jPrintCommandListener(log));
        }
        ftpClient.setConnectTimeout(getPoolConfig().getConnectTimeoutMills());
        ftpClient.connect(getPoolConfig().getHost(), getPoolConfig().getPort());
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new Exception("Received FTP response " + reply + " on create.");
        }
        boolean result = ftpClient.login(getPoolConfig().getUsername(), getPoolConfig().getPassword());
        if (!result) {
            String message = ftpClient + " login failed! userName:" + getPoolConfig().getUsername() + ", password:"
                    + getPoolConfig().getPassword();
            ftpClient.disconnect();
            log.error(message);
            throw new Exception(message);
        }
        ftpClient.setControlEncoding(getPoolConfig().getControlEncoding());
        ftpClient.setBufferSize(getPoolConfig().getBufferSize());
        ftpClient.setFileType(getPoolConfig().getFileType());
        ftpClient.setDataTimeout(Duration.ofMillis(getPoolConfig().getDataTimeoutMills()));
        ftpClient.setUseEPSVwithIPv4(getPoolConfig().isUseEPSVWithIPv4());
        if (getPoolConfig().isPassiveMode()) {
            //进入本地被动模式
            ftpClient.enterLocalPassiveMode();
        } else {
            //进入本地主动模式
            ftpClient.enterLocalActiveMode();
        }
        log.info("{} to be created consumed {} mills, enter local {} mode.", ftpClient, System.currentTimeMillis() - startMills, getPoolConfig().isPassiveMode() ? "passive" : "active");
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        // 此处需要区分logout与disconnect的关系。
        p.getObject().disconnect();
    }

    /**
     * 选取部分空闲对象进行计算决定是否驱逐，参考org.apache.commons.pool2.impl.GenericObjectPool#getNumTests()
     */
    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        boolean connected = false;
        FTPClient ftpClient = p.getObject();
        try {
            if (ftpClient != null) {
                connected = ftpClient.sendNoOp();
            }
        } catch (Exception e) {
            log.error("{} validateObject error: {}", ftpClient, e.getMessage());
        }
        return connected;
    }


}
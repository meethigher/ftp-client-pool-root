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
 * 参考自https://github.com/XiaZengming/FtpClientPool
 *
 * @author chenchuancheng
 * @since 2023/10/21 03:01
 */
public class FTPClientFactory extends BasePooledObjectFactory<FTPClient> {

    private static final Logger log = LoggerFactory.getLogger(FTPClientFactory.class);

    private final FTPPoolConfig poolConfig;

    public FTPClientFactory(FTPPoolConfig config) {
        this.poolConfig = config;
    }

    public FTPPoolConfig getPoolConfig() {
        return poolConfig;
    }

    @Override
    public FTPClient create() throws Exception {
        FTPClient ftpClient = new FTPClient();
        if (getPoolConfig().isDebug()) {
            //ftpClient.addProtocolCommandListener(new org.apache.commons.net.PrintCommandListener(System.out));
            ftpClient.addProtocolCommandListener(new Slf4jPrintCommandListener(log));
        }
        ftpClient.setConnectTimeout(getPoolConfig().getConnectTimeoutMills());
        try {
            ftpClient.connect(getPoolConfig().getHost(), getPoolConfig().getPort());
        } catch (Exception e) {
            log.error("{} {}", ftpClient, e.getMessage());
        }
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            log.error("{} failed to connect to the FTPServer {}:{}", ftpClient, getPoolConfig().getHost(), getPoolConfig().getPort());
            return null;
        } else {
            log.info("{} successfully connected to the FTPServer {}:{}", ftpClient, getPoolConfig().getHost(), getPoolConfig().getPort());
        }
        boolean result = ftpClient.login(getPoolConfig().getUsername(), getPoolConfig().getPassword());
        if (!result) {
            String message = ftpClient + " login failed! userName:" + getPoolConfig().getUsername() + ", password:"
                    + getPoolConfig().getPassword();
            log.error(message);
            throw new Exception(message);
        }
        ftpClient.setControlEncoding(getPoolConfig().getControlEncoding());
        ftpClient.setBufferSize(getPoolConfig().getBufferSize());
        ftpClient.setFileType(getPoolConfig().getFileType());
        ftpClient.setDataTimeout(Duration.ofMillis(getPoolConfig().getDataTimeoutMills()));
        ftpClient.setUseEPSVwithIPv4(getPoolConfig().isUseEPSVWithIPv4());
        if (getPoolConfig().isPassiveMode()) {
            log.info("{} enter local passive mode", ftpClient);
            //进入本地被动模式
            ftpClient.enterLocalPassiveMode();
        } else {
            log.info("{} enter local active mode", ftpClient);
            //进入本地主动模式
            ftpClient.enterLocalActiveMode();
        }
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        p.getObject().logout();
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
            //log.info("{} current connection status：{}", ftpClient, connected);
        } catch (Exception e) {
            log.error("{} validateObject error: {}", ftpClient, e.getMessage());
        }
        return connected;
    }


}
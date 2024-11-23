package top.meethigher.ftp.client.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.meethigher.ftp.client.pool.config.FTPPoolConfig;
import top.meethigher.ftp.client.pool.factory.FTPClientFactory;


/**
 * FTPClient连接池
 *
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @see <a href="https://github.com/XiaZengming/FtpClientPool">XiaZengming/FtpClientPool</a>
 * @since 2023/10/21 03:02
 */
public class FTPClientPool extends GenericObjectPool<FTPClient> {

    protected static final Logger log = LoggerFactory.getLogger(FTPClientPool.class);

    protected final FTPClientFactory factory;

    @Override
    public FTPClientFactory getFactory() {
        return factory;
    }

    public FTPPoolConfig getPoolConfig() {
        return factory.getPoolConfig();
    }

    public FTPClientPool(FTPClientFactory ftpClientFactory, boolean lazy) {
        super(ftpClientFactory, ftpClientFactory.getPoolConfig());
        this.factory = ftpClientFactory;
        if (!lazy) {
            try {
                // 急加载，立即初始化连接数量到minIdle
                this.preparePool();
            } catch (Exception e) {
                log.error("preparePool error", e);
            }
        }
        log.info("{} - Start completed.", factory.getPoolConfig().getPoolName());
    }

    public FTPClientPool(FTPClientFactory ftpClientFactory) {
        this(ftpClientFactory, true);
    }

    @Override
    public void close() {
        super.close();
        log.info("{} - Shutdown completed.", factory.getPoolConfig().getPoolName());
    }
}

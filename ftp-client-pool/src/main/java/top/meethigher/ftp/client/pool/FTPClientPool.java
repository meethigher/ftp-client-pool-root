package top.meethigher.ftp.client.pool;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.meethigher.ftp.client.pool.config.FTPPoolConfig;
import top.meethigher.ftp.client.pool.factory.FTPClientFactory;

import java.time.Duration;

/**
 * FTPClient连接池
 *
 * @author chenchuancheng
 * @since 2023/10/21 03:02
 */
public class FTPClientPool extends GenericObjectPool<FTPClient> {

    private static final Logger log = LoggerFactory.getLogger(FTPClientPool.class);

    private final FTPClientFactory factory;

    @Override
    public FTPClientFactory getFactory() {
        return factory;
    }

    public FTPPoolConfig getPoolConfig() {
        return factory.getPoolConfig();
    }

    public FTPClientPool(FTPClientFactory ftpClientFactory) {
        super(ftpClientFactory, ftpClientFactory.getPoolConfig());
        this.factory = ftpClientFactory;
        //启用空闲监测, 每隔60秒进行空闲连接监测。通过工厂validateObject进行验证，如果验证失败，则从池中移除
        //当然，也可以手动写任务调用this.evict()方法，进行释放
        this.setTimeBetweenEvictionRuns(Duration.ofMillis(this.factory.getPoolConfig().getPoolEvictIntervalMills()));
        this.setTestWhileIdle(true);
        log.info("{} - Start completed.", factory.getPoolConfig().getPoolName());
    }

    @Override
    public void close() {
        super.close();
        log.info("{} - Shutdown completed.", factory.getPoolConfig().getPoolName());
    }
}

package top.meethigher.ftp.client.pool.starter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.meethigher.ftp.client.pool.FTPClientPool;
import top.meethigher.ftp.client.pool.config.FTPPoolConfig;
import top.meethigher.ftp.client.pool.factory.FTPClientFactory;
import top.meethigher.ftp.client.pool.utils.FTPAutoReleaser;

@Configuration
public class FTPClientPoolAutoConfiguration {


    @Bean("ftpPoolConfigProperties")
    @ConfigurationProperties(prefix = "ftp-client.pool")
    public FTPPoolConfigProperties ftpPoolConfigProperties() {
        return new FTPPoolConfigProperties();
    }


    @Bean("ftpPoolConfig")
    @ConditionalOnMissingBean(FTPPoolConfig.class)
    public FTPPoolConfig ftpPoolConfig(@Qualifier("ftpPoolConfigProperties") FTPPoolConfigProperties prop) {
        FTPPoolConfig config = new FTPPoolConfig();
        config.setHost(prop.getHost());
        config.setPort(prop.getPort());
        config.setUsername(prop.getUsername());
        config.setPassword(prop.getPassword());
        config.setControlEncoding(prop.getControlEncoding());
        config.setBufferSize(prop.getBufferSize());
        config.setFileType(prop.getFileType());
        config.setUseEPSVWithIPv4(prop.isUseEPSVWithIPv4());
        config.setPassiveMode(prop.isPassiveMode());
        config.setPoolEvictIntervalMills(prop.getPoolEvictIntervalMills());
        config.setConnectTimeoutMills(prop.getConnectTimeoutMills());
        config.setDataTimeoutMills(prop.getDataTimeoutMills());
        config.setDebug(prop.isDebug());
        config.setJmxEnabled(prop.isJmxEnabled());
        config.setMinIdle(prop.getMinIdle());
        config.setMaxIdle(prop.getMaxIdle());
        config.setMaxTotal(prop.getMaxTotal());
        config.setTestOnBorrow(prop.isTestOnBorrow());
        config.setTestOnCreate(prop.isTestOnCreate());
        config.setTestOnReturn(prop.isTestOnReturn());
        config.setTestWhileIdle(prop.isTestWhileIdle());
        return config;
    }

    @Bean("ftpClientPool")
    @ConditionalOnMissingBean(FTPClientPool.class)
    public FTPClientPool ftpClientPool(@Qualifier("ftpPoolConfig") FTPPoolConfig ftpPoolConfig,
                                       @Qualifier("ftpPoolConfigProperties") FTPPoolConfigProperties prop) {
        return new FTPClientPool(new FTPClientFactory(ftpPoolConfig), prop.isLazy());
    }

    @Bean("ftpAutoReleaser")
    @ConditionalOnMissingBean(FTPAutoReleaser.class)
    public FTPAutoReleaser ftpAutoReleaser(@Qualifier("ftpClientPool") FTPClientPool ftpClientPool) {
        return new FTPAutoReleaser(ftpClientPool);
    }
}

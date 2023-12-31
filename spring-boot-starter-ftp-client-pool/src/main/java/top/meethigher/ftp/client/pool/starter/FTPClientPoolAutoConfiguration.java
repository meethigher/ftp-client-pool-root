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


    @Bean("ftpPoolConfig")
    @ConfigurationProperties(prefix = "ftp-client.pool")
    @ConditionalOnMissingBean(FTPPoolConfig.class)
    public FTPPoolConfig ftpPoolConfig() {
        return new FTPPoolConfig();
    }

    @Bean("ftpClientPool")
    @ConditionalOnMissingBean(FTPClientPool.class)
    public FTPClientPool ftpClientPool(@Qualifier("ftpPoolConfig") FTPPoolConfig ftpPoolConfig) {
        return new FTPClientPool(new FTPClientFactory(ftpPoolConfig));
    }

    @Bean("ftpAutoReleaser")
    @ConditionalOnMissingBean(FTPAutoReleaser.class)
    public FTPAutoReleaser ftpAutoReleaser(@Qualifier("ftpClientPool") FTPClientPool ftpClientPool) {
        return new FTPAutoReleaser(ftpClientPool);
    }
}

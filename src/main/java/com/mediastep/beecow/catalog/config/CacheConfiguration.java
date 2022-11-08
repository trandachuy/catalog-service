package com.mediastep.beecow.catalog.config;

import com.mediastep.gosell.shared.cache.resolver.AutoCreateCacheResolver;
import com.mediastep.gosell.shared.cache.resolver.redis.RedisAutoCreateCacheResolver;
import com.mediastep.gosell.shared.cache.util.CacheUtils;
import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
import org.hibernate.cache.jcache.ConfigSettings;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.redisson.jcache.configuration.RedissonConfiguration;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;

    @Bean
    public Config redissonConfig(JHipsterProperties jHipsterProperties,
                                 @Value("${spring.redis.database:0}") int redisDatabaseIndex) {
        URI redisUri = URI.create(jHipsterProperties.getCache().getRedis().getServer()[0]);

        Config config = new Config();
        if (jHipsterProperties.getCache().getRedis().isCluster()) {
            ReplicatedServersConfig replicatedServersConfig = config.useReplicatedServers()
                .setMasterConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setSlaveConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSlaveConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .addNodeAddress(jHipsterProperties.getCache().getRedis().getServer())
                .setReadMode(ReadMode.MASTER_SLAVE).setSubscriptionMode(SubscriptionMode.SLAVE)
                .setDatabase(redisDatabaseIndex);

            if (redisUri.getUserInfo() != null) {
                replicatedServersConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        } else {
            SingleServerConfig singleServerConfig = config
                .useSingleServer()
                .setConnectionPoolSize(jHipsterProperties.getCache().getRedis().getConnectionPoolSize())
                .setConnectionMinimumIdleSize(jHipsterProperties.getCache().getRedis().getConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(jHipsterProperties.getCache().getRedis().getSubscriptionConnectionPoolSize())
                .setAddress(jHipsterProperties.getCache().getRedis().getServer()[0])
                .setDatabase(redisDatabaseIndex);

            if (redisUri.getUserInfo() != null) {
                singleServerConfig.setPassword(redisUri.getUserInfo().substring(redisUri.getUserInfo().indexOf(':') + 1));
            }
        }
        return config;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson(Config config) {
        return Redisson.create(config);
    }

    @Bean
    public javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration(RedissonClient redisson, JHipsterProperties jHipsterProperties) {
        MutableConfiguration<Object, Object> jcacheConfig = new MutableConfiguration<>();

        jcacheConfig.setStatisticsEnabled(true);
        jcacheConfig.setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, jHipsterProperties.getCache().getRedis().getExpiration())));
        return RedissonConfiguration.fromInstance(redisson, jcacheConfig);
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cm) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cm);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer(RedissonClient redisson, JHipsterProperties jHipsterProperties) {
        return cm -> {
            javax.cache.configuration.Configuration<Object, Object> domainConfig = CacheUtils.jcacheConfiguration(redisson, jHipsterProperties.getCache().getRedis().getExpiration());
            CacheUtils.createCustomizeCacheInPackage("com.mediastep.beecow.catalog.domain", cm, domainConfig);
            // jhipster-needle-redis-add-entry
            javax.cache.configuration.Configuration<Object, Object> noExpiryConfig = CacheUtils.jcacheConfiguration(redisson, -1);
            CacheUtils.createCustomizeCacheConstants(Constants.CacheName.class, cm, noExpiryConfig);
//            CacheUtils.createCustomizeCacheConstants(CacheNames.class, cm, noExpiryConfig);
        };
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }

    @Bean("autoCreateCacheResolver")
    @Primary
    public AutoCreateCacheResolver autoCreateCacheResolver(CacheManager cacheManager, RedissonClient redisson) {
        Map<String, CacheConfig> configMap = new HashMap<>();
//        configMap.putAll(new RedisAutoCreateCacheResolver.CacheConfigBuilder()); // Specific config for each cache name
        return new RedisAutoCreateCacheResolver(cacheManager, redisson, configMap);
    }
}

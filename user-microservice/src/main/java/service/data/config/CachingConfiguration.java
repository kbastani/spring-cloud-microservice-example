package service.data.config;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@EnableCaching
@Configuration
public class CachingConfiguration implements CachingConfigurer {

    @Qualifier("couchbaseCacheManager")
    @Autowired(required = false)
    CacheManager couchbaseCacheManager;

    final Logger logger = LoggerFactory.getLogger(CachingConfigurer.class);

    @Bean
    @Override
    public CacheManager cacheManager() {
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();

        List<CacheManager> cacheManagers = Lists.newArrayList();

        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Collections.singletonList(
                new ConcurrentMapCache("cache")
        ));

        if (this.couchbaseCacheManager != null) {
            cacheManagers.add(this.couchbaseCacheManager);
            cacheManagers.add(simpleCacheManager);
        }

        compositeCacheManager.setCacheManagers(cacheManagers);
        compositeCacheManager.setFallbackToNoOpCache(true);

        return compositeCacheManager;
    }

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                logger.warn(cache.getName(), exception);
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                logger.warn(cache.getName(), exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                logger.warn(cache.getName(), exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                logger.warn(cache.getName(), exception);
            }
        };
    }
}

package com.verhoturkin.votingsystem.config;

import com.verhoturkin.votingsystem.model.Vote;
import com.verhoturkin.votingsystem.to.VoteDto;import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.net.URISyntaxException;
import java.time.Clock;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;



@Configuration
@ComponentScan({"com.verhoturkin.**.config", "com.verhoturkin.**.service", "com.verhoturkin.**.mapper"})
@EnableCaching
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        mapper.createTypeMap(Vote.class, VoteDto.class)
                .addMapping(vote -> vote.getUser().getId(), VoteDto::setUser_id)
                .addMapping(vote -> vote.getRestaurant().getId(), VoteDto::setRestaurant_id);
        return mapper;
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

//    @Bean
//    public CacheManager cacheManager() {
//        CachingProvider provider = Caching.getCachingProvider();
//        CacheManager cacheManager = provider.getCacheManager();
//        MutableConfiguration<Long, String> configuration =
//                new MutableConfiguration<Long, String>()
//                        .setTypes(Long.class, String.class)
//                        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY));
//        Cache<Long, String> cache = cacheManager.createCache("jCache", configuration);
//
//        return cacheManager;

//        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
//                .withCache("users", ).build();
//        return cacheManager;
//    }
//
//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("restaurants");
//    }

    @Bean
    public CacheManager cacheManager() throws URISyntaxException {
        MutableConfiguration<Long, String> configuration = new MutableConfiguration<Long, String>()
                        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY));
        Cache<Long, String> cache = Caching.getCachingProvider().getCacheManager().createCache("restaurants", configuration);
        return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager());

    }
}

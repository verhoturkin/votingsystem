package com.verhoturkin.votingsystem.config;

import com.verhoturkin.votingsystem.model.Vote;
import com.verhoturkin.votingsystem.to.VoteDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.time.Clock;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;



@Configuration
@ComponentScan({"com.verhoturkin.**.config", "com.verhoturkin.**.service", "com.verhoturkin.**.mapper"})
@EnableCaching
@EnableSwagger2
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


    @Bean
    public CacheManager cacheManager() {
        MutableConfiguration<Long, String> configuration = new MutableConfiguration<Long, String>()
                        .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_DAY));
        Cache<Long, String> cache = Caching.getCachingProvider().getCacheManager().createCache("restaurants", configuration);
        return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager());
    }

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("TITLE")
                .description("DESCRIPTION")
                .version("VERSION")
                .termsOfServiceUrl("http://terms-of-services.url")
                .license("LICENSE")
                .licenseUrl("http://url-to-license.com")
                .build();
    }
}

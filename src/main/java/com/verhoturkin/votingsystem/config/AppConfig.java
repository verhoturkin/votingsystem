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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.time.Clock;
import java.util.List;

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
                .addMapping(vote -> vote.getUser().getId(), VoteDto::setUserId)
                .addMapping(vote -> vote.getRestaurant().getId(), VoteDto::setRestaurantId);
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
        Caching.getCachingProvider().getCacheManager().createCache("restaurants", configuration);
        return new JCacheCacheManager(Caching.getCachingProvider().getCacheManager());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .pathMapping("/")
                .securitySchemes(List.of(new BasicAuth("basicAuth")))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Restaurant voting system REST API")
                .description("Autogenerated by Swagger2")
                .version("v1")
                .build();
    }
}

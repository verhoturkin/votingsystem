package com.verhoturkin.votingsystem.config;

import com.verhoturkin.votingsystem.model.Vote;
import com.verhoturkin.votingsystem.to.VoteDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
@ComponentScan({"com.verhoturkin.**.config", "com.verhoturkin.**.service", "com.verhoturkin.**.mapper"})
public class AppConfigTest {

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
        return Clock.fixed(Instant.parse(LocalDate.now().toString() + "T11:15:30.00Z"), ZoneId.of("UTC"));
    }
}

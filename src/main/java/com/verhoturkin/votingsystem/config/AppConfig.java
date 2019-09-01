package com.verhoturkin.votingsystem.config;

import com.verhoturkin.votingsystem.model.Dish;
import com.verhoturkin.votingsystem.to.DishDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
@ComponentScan({"com.verhoturkin.**.config", "com.verhoturkin.**.service", "com.verhoturkin.**.mapper"})
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        mapper.createTypeMap(Dish.class, DishDto.class).addMapping(dish -> dish.getRestaurant().getId(), DishDto::setRestaurant_id);
        return mapper;
    }
}

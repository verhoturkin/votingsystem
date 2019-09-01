package com.verhoturkin.votingsystem.util.mapper;

import com.verhoturkin.votingsystem.model.Restaurant;
import com.verhoturkin.votingsystem.to.RestaurantDto;
import com.verhoturkin.votingsystem.to.RestaurantWithDishesDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RestaurantMapper {

    private final ModelMapper mapper;

    @Autowired
    public RestaurantMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public RestaurantDto convertToDto(Restaurant restaurant) {
        return Objects.isNull(restaurant) ? null : mapper.map(restaurant, RestaurantDto.class);
    }

    public RestaurantWithDishesDto convertToDtoWithDishes(Restaurant restaurant) {
        RestaurantWithDishesDto restaurantWithDishesDto = Objects.isNull(restaurant) ? null : mapper.map(restaurant, RestaurantWithDishesDto.class);
        return restaurantWithDishesDto;
    }

    public Restaurant convertToEntity(RestaurantDto dto) {
        Restaurant restaurant = Objects.isNull(dto) ? null : mapper.map(dto, Restaurant.class);
        return restaurant;
    }
}

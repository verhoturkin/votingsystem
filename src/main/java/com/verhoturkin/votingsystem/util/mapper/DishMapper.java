package com.verhoturkin.votingsystem.util.mapper;

import com.verhoturkin.votingsystem.model.Dish;
import com.verhoturkin.votingsystem.to.DishDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DishMapper {

    private final ModelMapper mapper;


    @Autowired
    public DishMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public DishDto convertToDto(Dish dish) {
        return Objects.isNull(dish) ? null : mapper.map(dish, DishDto.class);
    }

    public Dish convertToEntity(DishDto dishDto) {
        return Objects.isNull(dishDto) ? null : mapper.map(dishDto, Dish.class);
    }
}

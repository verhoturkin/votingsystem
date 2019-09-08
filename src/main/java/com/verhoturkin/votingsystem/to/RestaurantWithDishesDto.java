package com.verhoturkin.votingsystem.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@ApiModel(value = "RestaurantWithDishesDTO", description = "Restaurant data transfer object with list of DishDTOs")
public class RestaurantWithDishesDto extends RestaurantDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "List of DishDTO")
    @NotNull
    private List<DishDto> dishes;

    public RestaurantWithDishesDto() {
    }

    public RestaurantWithDishesDto(Integer id, String name, List<DishDto> dishes) {
        super(id, name);
        this.dishes = dishes;
    }

    public List<DishDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDto> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "RestaurantWithDishesDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishes=" + dishes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantWithDishesDto that = (RestaurantWithDishesDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(dishes, that.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dishes);
    }
}

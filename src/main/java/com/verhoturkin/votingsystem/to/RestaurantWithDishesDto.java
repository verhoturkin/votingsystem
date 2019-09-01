package com.verhoturkin.votingsystem.to;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class RestaurantWithDishesDto extends RestaurantDto {

    @NotNull
    private List<DishDto> dishes;

    public RestaurantWithDishesDto() {
    }

    public RestaurantWithDishesDto(RestaurantWithDishesDto r) {
        this(r.getId(), r.getName(), r.getDishes());
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
                "name='" + name + '\'' +
                ", dishes=" + dishes +
                ", id=" + id +
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

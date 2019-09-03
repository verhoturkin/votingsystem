package com.verhoturkin.votingsystem;

import com.verhoturkin.votingsystem.to.RestaurantDto;
import com.verhoturkin.votingsystem.to.RestaurantWithDishesDto;

import java.util.List;

import static com.verhoturkin.votingsystem.DishTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestHelper {


    public static final int RESTAURANT1_ID = 100003;
    public static final RestaurantDto RESTAURANT1 = new RestaurantDto(RESTAURANT1_ID, "McDonalds");
    public static final RestaurantDto RESTAURANT2 = new RestaurantDto(100004, "KFC");
    public static final RestaurantDto RESTAURANT3 = new RestaurantDto(100005, "Burger King");

    public static final List<RestaurantDto> RESTAURANTS = List.of(RESTAURANT3, RESTAURANT2, RESTAURANT1);

    public static final RestaurantWithDishesDto RESTAURANT1_MENU = new RestaurantWithDishesDto(RESTAURANT1_ID, "McDonalds", List.of(DISH2, DISH1));
    public static final RestaurantWithDishesDto RESTAURANT2_MENU = new RestaurantWithDishesDto(100004, "KFC", List.of(DISH7, DISH8));
    public static final RestaurantWithDishesDto RESTAURANT3_MENU = new RestaurantWithDishesDto(100005, "Burger King", List.of(DISH5, DISH4));

    public static final List<RestaurantDto> RESTAURANTS_MENU = List.of(RESTAURANT3_MENU, RESTAURANT2_MENU, RESTAURANT1_MENU);

    public static void assertMatch(RestaurantDto actual, RestaurantDto expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "dishes");
    }

    public static void assertMatch(Iterable<RestaurantDto> actual, RestaurantDto... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<RestaurantDto> actual, Iterable<RestaurantDto> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("dishes").isEqualTo(expected);
    }
}

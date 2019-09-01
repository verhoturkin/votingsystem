package com.verhoturkin.votingsystem;

import com.verhoturkin.votingsystem.to.RestaurantDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantTestHelper {


    public static final int RESTAURANT1_ID = 100003;
    public static final RestaurantDto RESTAURANT1 = new RestaurantDto(RESTAURANT1_ID, "McDonalds");
    public static final RestaurantDto RESTAURANT2 = new RestaurantDto(100004, "KFC");
    public static final RestaurantDto RESTAURANT3 = new RestaurantDto(100005, "Burger King");

    public static final List<RestaurantDto> RESTAURANTS = List.of(RESTAURANT3, RESTAURANT2, RESTAURANT1);

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

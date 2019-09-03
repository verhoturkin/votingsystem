package com.verhoturkin.votingsystem;

import com.verhoturkin.votingsystem.to.DishDto;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DishTestHelper {
    public static final int DISH1_ID = 100006;
    public static final DishDto DISH1 = new DishDto(DISH1_ID, "Биг Мак", LocalDate.of(2015, 05, 30), 13501);
    public static final DishDto DISH2 = new DishDto(100007, "Чикен Премьер", LocalDate.of(2015, 05, 30), 13990);
    public static final DishDto DISH3 = new DishDto(100008, "Биг Тейсти", LocalDate.of(2015, 05, 31), 24980);

    public static final List<DishDto> DISHES = List.of(DISH3, DISH2, DISH1);

    public static void assertMatch(DishDto actual, DishDto expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<DishDto> actual, DishDto... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<DishDto> actual, Iterable<DishDto> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}

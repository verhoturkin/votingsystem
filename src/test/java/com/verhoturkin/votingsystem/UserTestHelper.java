package com.verhoturkin.votingsystem;

import com.verhoturkin.votingsystem.model.Role;
import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.to.UserDto;
import com.verhoturkin.votingsystem.web.json.JsonUtil;

import java.util.List;

import static com.verhoturkin.votingsystem.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;


public class UserTestHelper {
    public static final int USER1_ID = START_SEQ;
    public static final int USER2_ID = START_SEQ + 1;
    public static final int ADMIN_ID = START_SEQ + 2;

    public static final User USER1 = new User(USER1_ID, "User1", "user1@yandex.ru", "password", Role.ROLE_USER);
    public static final User USER2 = new User(USER2_ID, "User2", "user2@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN, Role.ROLE_USER);

    public static final UserDto USER1_DTO = new UserDto(USER1_ID, "User1", "user1@yandex.ru", "{noop}password");
    public static final UserDto USER2_DTO = new UserDto(USER2_ID, "Юзер2", "user2@yandex.ru", "{noop}password");
    public static final UserDto ADMIN_DTO = new UserDto(ADMIN_ID, "Admin", "admin@gmail.com", "{noop}admin");

    public static final List<UserDto> DTOS = List.of(ADMIN_DTO, USER1_DTO, USER2_DTO);

    public static void assertMatch(UserDto actual, UserDto expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "password");
    }

    public static void assertMatch(Iterable<UserDto> actual, UserDto... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<UserDto> actual, Iterable<UserDto> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("password").isEqualTo(expected);
    }

    public static String jsonWithPassword(UserDto user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

}

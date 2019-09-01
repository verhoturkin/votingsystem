package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.repository.UserRepository;
import com.verhoturkin.votingsystem.to.UserDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.UserMapper;
import com.verhoturkin.votingsystem.web.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.UserTestHelper.*;
import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.readValue;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.writeValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileUserControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = REST_V1 + "/users/profile";

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Test
    void getProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(USER1_DTO)));
    }

    @Test
    void getProfileUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteProfile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(repository.findAllByOrderByNameAscEmailAsc().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList()), ADMIN_DTO, USER2_DTO);

    }

    @Test
    void register() throws Exception {
        UserDto expected = new UserDto(null, "New", "new@gmail.com", "newPass");

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(REST_V1 + "/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(expected, "newPass")))
                .andDo(print())
                .andExpect(status().isCreated());

        UserDto returned = readValue(actions.andReturn().getResponse().getContentAsString(), UserDto.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(repository.findAllByOrderByNameAscEmailAsc().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList()), ADMIN_DTO, expected, USER1_DTO, USER2_DTO);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerDuplicate() throws Exception {
        UserDto expected = new UserDto(null, "New", "user1@yandex.ru", "newPass");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_V1 + "/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(expected, "newPass")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void registerInvalid() throws Exception {
        UserDto expected = new UserDto(null, null, "", "newPass");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_V1 + "/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void profileUpdate() throws Exception {
        UserDto updated = new UserDto(USER1_DTO);
        updated.setName("UpdatedUser");
        updated.setPassword("UpdatedPass");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, "UpdatedPass")))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(mapper.convertToDto(repository.findById(USER1_ID)
                .orElseThrow(NotFoundException::new)), updated);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void profileUpdateDuplicate() throws Exception {
        UserDto updated = new UserDto(USER1_DTO);
        updated.setEmail("admin@gmail.com");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated, "password")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void profileUpdateInvalid() throws Exception {
        UserDto updated = new UserDto(USER1_DTO);
        updated.setName(null);
        updated.setEmail("");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(USER1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}

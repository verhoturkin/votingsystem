package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.repository.RestaurantRepository;
import com.verhoturkin.votingsystem.to.RestaurantDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.RestaurantMapper;
import com.verhoturkin.votingsystem.web.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.RestaurantTestHelper.*;
import static com.verhoturkin.votingsystem.UserTestHelper.ADMIN;
import static com.verhoturkin.votingsystem.UserTestHelper.USER1;
import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.readValue;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.writeValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = REST_V1 + "/restaurants/";

    @Autowired
    private RestaurantRepository repository;

    @Autowired
    private RestaurantMapper mapper;

    //Admin part

    @Test
    void create() throws Exception {
        RestaurantDto expected = new RestaurantDto(null, "New");
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());

        RestaurantDto returned = readValue(actions.andReturn().getResponse().getContentAsString(), RestaurantDto.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(repository.findAllByOrderByNameAsc().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList()), RESTAURANT3, RESTAURANT2, RESTAURANT1, expected);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        RestaurantDto expected = new RestaurantDto(null, "KFC");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createInvalid() throws Exception {
        RestaurantDto expected = new RestaurantDto(null, "");
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(RESTAURANTS)));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(RESTAURANT1)));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void update() throws Exception {
        RestaurantDto updated = new RestaurantDto(RESTAURANT1);
        updated.setName("Updated");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(mapper.convertToDto(repository.findById(RESTAURANT1_ID)
                .orElseThrow(NotFoundException::new)), updated);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        RestaurantDto updated = new RestaurantDto(RESTAURANT1);
        updated.setName("KFC");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateInvalid() throws Exception {
        RestaurantDto updated = new RestaurantDto(RESTAURANT1);
        updated.setName("");
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(repository.findAllByOrderByNameAsc().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList()), RESTAURANT3, RESTAURANT2);
    }

    //User part

    @Test
    void getToday() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "today")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
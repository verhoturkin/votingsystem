package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.repository.DishRepository;
import com.verhoturkin.votingsystem.to.DishDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.DishMapper;
import com.verhoturkin.votingsystem.web.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.DishTestHelper.*;
import static com.verhoturkin.votingsystem.RestaurantTestHelper.RESTAURANT1_ID;
import static com.verhoturkin.votingsystem.UserTestHelper.ADMIN;
import static com.verhoturkin.votingsystem.UserTestHelper.USER1;
import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.readValue;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.writeValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = REST_V1 + "/restaurants/" + RESTAURANT1_ID + "/dishes/";

    @Autowired
    private DishRepository repository;

    @Autowired
    private DishMapper mapper;

    @Test
    void create() throws Exception {
        DishDto expected = new DishDto(null, "New", LocalDate.now(), 50);
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());

        DishDto returned = readValue(actions.andReturn().getResponse().getContentAsString(), DishDto.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatch(repository.findAllByRestaurantIdOrderByPriceDesc(RESTAURANT1_ID).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList()), DISH3, DISH2, DISH1, expected);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createInvalid() throws Exception {
        DishDto expected = new DishDto(null, "", LocalDate.now(), 0);
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
                .andExpect(content().json(writeValue(DISHES), true));
    }

    @Test
    void getAllByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "byDate")
                .param("date", "2015-05-30")
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(List.of(DISH2, DISH1)), true));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(DISH1), true));
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
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        DishDto updated = new DishDto(DISH1);
        updated.setName("Updated");
        updated.setPrice(2);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(mapper.convertToDto(repository.findById(DISH1_ID)
                .orElseThrow(NotFoundException::new)), updated);

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateInvalid() throws Exception {
        DishDto updated = new DishDto(DISH1);
        updated.setName("");
        updated.setPrice(0);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateWrongRestaurant() throws Exception {
        DishDto updated = new DishDto(DISH1);
        updated.setName("Updated");
        updated.setPrice(2);
        mockMvc.perform(MockMvcRequestBuilders.put(REST_V1 + "/restaurants/" + 1 + "/dishes/" + DISH1_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(repository.findAllByRestaurantIdOrderByPriceDesc(RESTAURANT1_ID).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList()), DISH3, DISH2);
    }

    @Test
    void deleteWrongRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_V1 + "/restaurants/" + 1 + "/dishes/" + DISH1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

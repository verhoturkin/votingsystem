package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.to.VoteDto;
import com.verhoturkin.votingsystem.web.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.verhoturkin.votingsystem.RestaurantTestHelper.RESTAURANT1_ID;
import static com.verhoturkin.votingsystem.UserTestHelper.*;
import static com.verhoturkin.votingsystem.VoteTestData.VOTE1;
import static com.verhoturkin.votingsystem.VoteTestData.assertMatch;
import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.readValue;
import static com.verhoturkin.votingsystem.web.json.JsonUtil.writeValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VoteControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = REST_V1 + "/restaurants/" + RESTAURANT1_ID + "/votes/";

    @Test
    void create() throws Exception {
        VoteDto expected = new VoteDto(null, LocalDate.of(2015, 05, 30), USER2_ID, RESTAURANT1_ID);
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER2)))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteDto returned = readValue(actions.andReturn().getResponse().getContentAsString(), VoteDto.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
    }

    @Test
    void createExpired() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void createByAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void getCurrent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "current")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(writeValue(VOTE1), true));
    }

    @Test
    void getCount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "count")
                .with(userHttpBasic(USER1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1"));
    }
}

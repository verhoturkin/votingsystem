package com.verhoturkin.votingsystem.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(value = "VoteDTO", description = "Vote data transfer object.")
public class VoteDto extends BaseDto {

    @ApiModelProperty(notes = "Date in ISO format ('YYYY-MM-DD')")
    @NotNull
    private LocalDate date;

    @ApiModelProperty(notes = "ID of vote's user")
    @NotNull
    private Integer user_id;

    @ApiModelProperty(notes = "ID of vote's restaurant ")
    @NotNull
    private Integer restaurant_id;

    public VoteDto() {
    }

    public VoteDto(Integer id, LocalDate date, Integer user_id, Integer restaurant_id) {
        super(id);
        this.date = date;
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }


}

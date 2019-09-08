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
    private Integer userId;

    @ApiModelProperty(notes = "ID of vote's restaurant ")
    @NotNull
    private Integer restaurantId;

    public VoteDto() {
    }

    public VoteDto(Integer id, LocalDate date, Integer userId, Integer restaurantId) {
        super(id);
        this.date = date;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }


}

package com.verhoturkin.votingsystem.to;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class VoteDto extends BaseDto {

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer user_id;

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

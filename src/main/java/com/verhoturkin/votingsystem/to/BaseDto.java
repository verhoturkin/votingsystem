package com.verhoturkin.votingsystem.to;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public abstract class BaseDto implements Serializable {

    @ApiModelProperty(notes = "The database generated ID")
    protected Integer id;

    public BaseDto() {
    }

    public BaseDto(Integer id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

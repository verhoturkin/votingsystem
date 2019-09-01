package com.verhoturkin.votingsystem.to;

public abstract class BaseDto {

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

package com.verhoturkin.votingsystem.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@ApiModel(value = "RestaurantDTO", description = "Restaurant data transfer object.")
public class RestaurantDto extends BaseDto {

    @ApiModelProperty(notes = "Min length = 2, Max length = 255")
    @NotBlank
    @Size(min = 2, max = 255)
    protected String name;

    public RestaurantDto() {
    }

    public RestaurantDto(RestaurantDto r) {
        this(r.getId(), r.getName());
    }

    public RestaurantDto(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RestaurantDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestaurantDto that = (RestaurantDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

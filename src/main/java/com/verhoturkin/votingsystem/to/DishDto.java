package com.verhoturkin.votingsystem.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@ApiModel(value = "DishDTO", description = "Dish data transfer object.")
public class DishDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "Min length = 2, Max length = 255")
    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    @ApiModelProperty(notes = "Date in ISO format ('YYYY-MM-DD')")
    @NotNull
    private LocalDate date;

    @ApiModelProperty(notes = "Price in cents. Min = 1")
    @NotNull
    @Range(min = 1)
    private int price;

    public DishDto() {
    }

    public DishDto(DishDto d) {
        this(d.getId(), d.getName(), d.getDate(), d.getPrice());
    }

    public DishDto(Integer id, String name, LocalDate date, int price) {
        super(id);
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "DishDto{" +
                "id=" + id +
                ". name='" + name + '\'' +
                ", date=" + date +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DishDto that = (DishDto) o;
        return price == that.price &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, price);
    }
}

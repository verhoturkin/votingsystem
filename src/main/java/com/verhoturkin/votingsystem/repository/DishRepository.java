package com.verhoturkin.votingsystem.repository;


import com.verhoturkin.votingsystem.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    List<Dish> findAllByRestaurantIdOrderByPriceDesc(int restaurantId);

    List<Dish> findAllByRestaurantIdAndDateOrderByPriceDesc(int restaurantId, LocalDate date);

    Optional<Dish> findByIdAndRestaurantId(int id, int restaurantId);
}

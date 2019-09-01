package com.verhoturkin.votingsystem.repository;


import com.verhoturkin.votingsystem.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    List<Dish> findAllByRestaurantIdOrderByPriceDesc(int restaurantId);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant WHERE d.id=:id")
    Optional<Dish> findByIdWithRestaurant(@Param("id") int id);
}

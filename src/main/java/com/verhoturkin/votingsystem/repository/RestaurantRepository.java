package com.verhoturkin.votingsystem.repository;


import com.verhoturkin.votingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("SELECT DISTINCT r FROM Restaurant r JOIN FETCH r.dishes d WHERE d.date= :date ORDER BY r.name")
    List<Restaurant> findAllWithDishes(@Param("date") LocalDate date);

    List<Restaurant> findAllByOrderByNameAsc();
}

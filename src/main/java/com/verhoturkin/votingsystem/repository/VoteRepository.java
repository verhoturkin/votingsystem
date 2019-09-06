package com.verhoturkin.votingsystem.repository;


import com.verhoturkin.votingsystem.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> findByDateAndUserId(LocalDate date, int userId);

    long countByDateAndRestaurantId(LocalDate date, int restaurantId);

    List<Vote> findAllByRestaurantId(int restaurantId);

    List<Vote> findAllByRestaurantIdAndDate(int restaurantId, LocalDate date);
}


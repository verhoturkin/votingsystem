package com.verhoturkin.votingsystem.service;

import com.verhoturkin.votingsystem.model.Restaurant;
import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.model.Vote;
import com.verhoturkin.votingsystem.repository.RestaurantRepository;
import com.verhoturkin.votingsystem.repository.VoteRepository;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.exception.VotingTimeExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VoteService {

    private static final LocalTime STOP = LocalTime.of(11, 0);

    private final VoteRepository repository;

    private final RestaurantRepository restaurantRepository;

    private final Clock clock;

    @Autowired
    public VoteService(VoteRepository repository, RestaurantRepository restaurantRepository, Clock clock) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.clock = clock;
    }

    public Vote save(User user, int restaurantId) {
        LocalDate now = LocalDate.now(clock);

        boolean expired = LocalTime.now(clock).isAfter(STOP);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(NotFoundException::new);

        Vote vote = repository.findByDateAndUserId(now, user.getId())
                .orElse(new Vote(null, LocalDate.now(clock), user, restaurant));

        if (!vote.isNew() && expired) {
            throw new VotingTimeExpiredException();
        } else {
            vote.setRestaurant(restaurant);
        }

        return repository.save(vote);
    }

    public Vote get(User user) {
        return repository.findByDateAndUserId(LocalDate.now(clock), user.getId()).orElseThrow(NotFoundException::new);
    }

    public List<Vote> findAllByRestaurantId(int restaurantId) {
        return repository.findAllByRestaurantIdOrderByDateDesc(restaurantId);
    }

    public List<Vote> findAllByDate(LocalDate date) {
        return repository.findAllByDateOrderByRestaurantIdAsc(date);
    }
}

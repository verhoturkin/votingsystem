package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.Dish;
import com.verhoturkin.votingsystem.repository.DishRepository;
import com.verhoturkin.votingsystem.repository.RestaurantRepository;
import com.verhoturkin.votingsystem.to.DishDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@RestController
@RequestMapping(value = REST_V1 + "/restaurants/{restaurantId}/dishes")
public class DishController {

    private final DishRepository dishRepository;

    private final RestaurantRepository restaurantRepository;

    private final DishMapper mapper;

    @Autowired
    public DishController(DishRepository repository, RestaurantRepository restaurantRepository, DishMapper mapper) {
        this.dishRepository = repository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public ResponseEntity<DishDto> create(@RequestBody @Valid DishDto dishDto, @PathVariable int restaurantId) {
        Dish dish = mapper.convertToEntity(dishDto);
        dish.setRestaurant(restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundException::new));

        DishDto created = mapper.convertToDto(dishRepository.save(dish));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/restaurants/{restaurantId}/dishes/{dishId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<DishDto> getAll(@PathVariable int restaurantId, @RequestParam(required = false) LocalDate date) {
        List<Dish> dishes = Objects.isNull(date) ?
                dishRepository.findAllByRestaurantIdOrderByPriceDesc(restaurantId) :
                dishRepository.findAllByRestaurantIdAndDateOrderByPriceDesc(restaurantId, date);
        return dishes.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{dishId}")
    public DishDto get(@PathVariable int restaurantId, @PathVariable int dishId) {
        return mapper.convertToDto(dishRepository.findByIdAndRestaurantId(dishId, restaurantId)
                .orElseThrow(NotFoundException::new));
    }

    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(@PathVariable int restaurantId, @PathVariable int dishId, @RequestBody @Valid DishDto dishDto) {
        dishRepository.findByIdAndRestaurantId(dishId, restaurantId)
                .orElseThrow(NotFoundException::new);

        dishRepository.save(mapper.convertToEntity(dishDto));
    }

    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int dishId) {
        dishRepository.findByIdAndRestaurantId(dishId, restaurantId)
                .orElseThrow(NotFoundException::new);

        dishRepository.deleteById(dishId);
    }


}

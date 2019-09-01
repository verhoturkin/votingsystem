package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.Restaurant;
import com.verhoturkin.votingsystem.repository.RestaurantRepository;
import com.verhoturkin.votingsystem.to.RestaurantDto;
import com.verhoturkin.votingsystem.to.RestaurantWithDishesDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.RestaurantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@RestController
@RequestMapping(value = REST_V1 + "/restaurants")
public class RestaurantController {

    private final RestaurantRepository repository;

    private final RestaurantMapper mapper;

    @Autowired
    public RestaurantController(RestaurantRepository repository, RestaurantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    //Admin part

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<RestaurantDto> create(@RequestBody @Valid RestaurantDto dto) {
        RestaurantDto created = mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<RestaurantDto> getAll() {
        List<Restaurant> restaurants = repository.findAllByOrderByNameAsc();
        return restaurants.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public RestaurantDto get(@PathVariable int id) {
        return mapper.convertToDto(repository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody @Valid RestaurantDto dto) {
        repository.save(mapper.convertToEntity(dto));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    //User part

    //TODO change date to .now()
    @GetMapping(value = "/today", produces = MediaType.APPLICATION_JSON_VALUE)
    private List<RestaurantWithDishesDto> getToday() {
        List<Restaurant> restaurants = repository.findAllWithDishes(LocalDate.of(2015, 05, 30));
        return restaurants.stream()
                .map(mapper::convertToDtoWithDishes)
                .collect(Collectors.toList());
    }
}

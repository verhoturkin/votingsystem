package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.Dish;
import com.verhoturkin.votingsystem.repository.DishRepository;
import com.verhoturkin.votingsystem.repository.RestaurantRepository;
import com.verhoturkin.votingsystem.to.DishDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.DishMapper;
import io.swagger.annotations.*;
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
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@Api(tags = "/restaurants/{id}/dishes", value = "/dishes", description = "Operations with Dishes", authorizations = {@Authorization(value = "basicAuth")})
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
})
@RestController
@RequestMapping(value = REST_V1 + "/restaurants/{restaurantId}/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "Create", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created", response = DishDto.class),
            @ApiResponse(code = 404, message = "Not found restaurant with given Id"),
            @ApiResponse(code = 422, message = "DTO validation failed")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public ResponseEntity<DishDto> create(@ApiParam(value = "Dish DTO", required = true) @RequestBody @Valid DishDto dishDto,
                                          @ApiParam(value = "Restaurant Id", required = true) @PathVariable int restaurantId) {
        Dish dish = mapper.convertToEntity(dishDto);
        dish.setRestaurant(restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundException::new));

        DishDto created = mapper.convertToDto(dishRepository.save(dish));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/restaurants/{restaurantId}/dishes/{dishId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Get all", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = DishDto.class, responseContainer = "List")})
    @GetMapping
    public List<DishDto> getAll(@ApiParam(value = "Restaurant Id", required = true) @PathVariable int restaurantId) {
        List<Dish> dishes = dishRepository.findAllByRestaurantIdOrderByPriceDesc(restaurantId);
        return dishes.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get all by date", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = DishDto.class, responseContainer = "List")})
    @GetMapping("/byDate")
    public List<DishDto> getAllByDate(@ApiParam(value = "Restaurant Id", required = true) @PathVariable int restaurantId,
                                      @ApiParam(value = "Date in ISO format ('YYYY-MM-DD')", required = true) @RequestParam LocalDate date) {
        List<Dish> dishes = dishRepository.findAllByRestaurantIdAndDateOrderByPriceDesc(restaurantId, date);
        return dishes.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = DishDto.class),
            @ApiResponse(code = 404, message = "Not found dish with given Id")
    })
    @GetMapping("/{dishId}")
    public DishDto get(@ApiParam(value = "Restaurant Id", required = true) @PathVariable int restaurantId,
                       @ApiParam(value = "Dish Id", required = true) @PathVariable int dishId) {
        return mapper.convertToDto(dishRepository.findByIdAndRestaurantId(dishId, restaurantId)
                .orElseThrow(NotFoundException::new));
    }

    @ApiOperation(value = "Update", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully updated"),
            @ApiResponse(code = 404, message = "Not found dish with given Id"),
            @ApiResponse(code = 422, message = "DTO validation failed")
    })
    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@ApiParam(value = "Restaurant Id", required = true) @PathVariable int restaurantId,
                       @ApiParam(value = "Dish Id", required = true) @PathVariable int dishId,
                       @ApiParam(value = "DishDTO", required = true) @RequestBody @Valid DishDto dishDto) {
        dishRepository.findByIdAndRestaurantId(dishId, restaurantId)
                .orElseThrow(NotFoundException::new);

        dishRepository.save(mapper.convertToEntity(dishDto));
    }

    @ApiOperation(value = "Delete", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found dish with given Id")
    })
    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@ApiParam(value = "Restaurant Id", required = true) @PathVariable int restaurantId,
                       @ApiParam(value = "Dish Id", required = true) @PathVariable int dishId) {
        dishRepository.findByIdAndRestaurantId(dishId, restaurantId)
                .orElseThrow(NotFoundException::new);

        dishRepository.deleteById(dishId);
    }
}

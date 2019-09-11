package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.Restaurant;
import com.verhoturkin.votingsystem.repository.RestaurantRepository;
import com.verhoturkin.votingsystem.to.RestaurantDto;
import com.verhoturkin.votingsystem.to.RestaurantWithDishesDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.RestaurantMapper;
import io.swagger.annotations.*;
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

@Api(tags = "/restaurants", value = "/restaurants", description = "Operations with Restaurants", authorizations = {@Authorization(value = "basicAuth")})
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
})
@RestController
@RequestMapping(value = REST_V1 + "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    private final RestaurantRepository repository;

    private final RestaurantMapper mapper;

    @Autowired
    public RestaurantController(RestaurantRepository repository, RestaurantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    //Admin part

    @ApiOperation(value = "Create", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created", response = RestaurantDto.class),
            @ApiResponse(code = 422, message = "DTO validation failed")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RestaurantDto> create(@ApiParam(value = "Restaurant DTO", required = true) @RequestBody @Valid RestaurantDto dto) {
        RestaurantDto created = mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/restaurants/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Get all", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = RestaurantDto.class, responseContainer = "List"),
    })
    @GetMapping
    public List<RestaurantDto> getAll() {
        List<Restaurant> restaurants = repository.findAllByOrderByNameAsc();
        return restaurants.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = RestaurantDto.class),
            @ApiResponse(code = 404, message = "Not found restaurant with given Id")
    })
    @GetMapping(value = "/{id}")
    public RestaurantDto get(@ApiParam(value = "Restaurant Id", required = true) @PathVariable int id) {
        return mapper.convertToDto(repository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    @ApiOperation(value = "Update", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully updated"),
            @ApiResponse(code = 404, message = "Not found restaurant with given Id"),
            @ApiResponse(code = 422, message = "DTO validation failed")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@ApiParam(value = "Restaurant Id", required = true) @PathVariable int id,
                       @ApiParam(value = "Restaurant DTO", required = true) @RequestBody @Valid RestaurantDto dto) {
        repository.save(mapper.convertToEntity(dto));
    }

    @ApiOperation(value = "Delete", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found restaurant with given Id")
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@ApiParam(value = "Restaurant ID", required = true) @PathVariable int id) {
        repository.deleteById(id);
    }

    //User part

    @ApiOperation(value = "Get Menu", notes = "Accessible by users having one of the following roles: USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = RestaurantWithDishesDto.class),
    })
    @GetMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RestaurantWithDishesDto> getMenu() {
        List<Restaurant> restaurants = repository.findAllWithDishes(LocalDate.now());

        return restaurants.stream()
                .map(mapper::convertToDtoWithDishes)
                .collect(Collectors.toList());
    }
}

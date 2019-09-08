package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.repository.UserRepository;
import com.verhoturkin.votingsystem.to.UserDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.UserMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@Api(value = "/users", description = "Operations with Users", authorizations = {@Authorization(value = "basicAuth")}, tags = "/users")
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
})
@RestController
@RequestMapping(value = REST_V1 + "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository repository;

    private final UserMapper mapper;

    @Autowired
    public UserController(UserRepository repository, UserMapper modelMapper) {
        this.repository = repository;
        this.mapper = modelMapper;
    }

    @ApiOperation(value = "Create", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created", response = UserDto.class),
            @ApiResponse(code = 422, message = "DTO validation failed")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserDto> create(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto userDto) {
        UserDto created = mapper.convertToDto(repository.save(mapper.convertToEntity(userDto)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Get all", notes = "Accessible by users having one of the following roles: ADMIN", tags = "/users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = UserDto.class, responseContainer = "List"),
    })
    @GetMapping
    public List<UserDto> getAll() {
        List<User> users = repository.findAllByOrderByNameAscEmailAsc();
        return users.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get", notes = "Accessible by users having one of the following roles: ADMIN", tags = "/users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = UserDto.class),
            @ApiResponse(code = 404, message = "Not found user with given Id")
    })
    @GetMapping("/{id}")
    public UserDto get(@ApiParam(value = "User ID", required = true) @PathVariable int id) {
        return mapper.convertToDto(repository.findById(id).orElseThrow(NotFoundException::new));
    }

    @ApiOperation(value = "Get by email", notes = "Accessible by users having one of the following roles: ADMIN", tags = "/users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = UserDto.class),
            @ApiResponse(code = 404, message = "Not found user with given email")
    })
    @GetMapping("/by")
    public UserDto getByEmail(@ApiParam(value = "User email", required = true) @RequestParam String email) {
        return mapper.convertToDto(repository.findByEmail(email).orElseThrow(NotFoundException::new));
    }

    @ApiOperation(value = "Update", notes = "Accessible by users having one of the following roles: ADMIN", tags = "/users")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully updated"),
            @ApiResponse(code = 404, message = "Not found user with given Id"),
            @ApiResponse(code = 422, message = "DTO validation failed")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto userDto,
                       @ApiParam(value = "User Id", required = true) @PathVariable int id) {
        repository.save(mapper.convertToEntity(userDto));
    }

    @ApiOperation(value = "Delete", notes = "Accessible by users having one of the following roles: ADMIN", tags = "/users")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted"),
            @ApiResponse(code = 404, message = "Not found user with given Id")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@ApiParam(value = "User Id", required = true) @PathVariable int id) {
        repository.deleteById(id);
    }

}

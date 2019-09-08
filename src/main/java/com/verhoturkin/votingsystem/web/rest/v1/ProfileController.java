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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@Api(value = "/profile", description = "Operations with user profile ", authorizations = {@Authorization(value = "basicAuth")}, tags = "/profile")
@ApiResponses(value = {
        ,
})
@RestController
@RequestMapping(value = REST_V1 + "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private final UserRepository repository;

    private final UserMapper mapper;

    @Autowired
    public ProfileController(UserRepository repository, UserMapper modelMapper) {
        this.repository = repository;
        this.mapper = modelMapper;
    }

    @ApiOperation(value = "Get current user", notes = "Accessible by users having one of the following roles: USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = UserDto.class),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")})
    @GetMapping()
    public UserDto getProfile(@AuthenticationPrincipal User user) {
        return mapper.convertToDto(repository.findById(user.getId()).orElseThrow(NotFoundException::new));
    }

    @ApiOperation(value = "Delete current user", notes = "Accessible by users having one of the following roles: USER")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully deleted"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden")})
    @DeleteMapping()
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User user) {
        repository.deleteById(user.getId());
    }

    @ApiOperation(value = "Register new user", notes = "Accessible without authorization")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully registered", response = UserDto.class),
            @ApiResponse(code = 422, message = "DTO validation failed")})
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserDto> register(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto userDto) {
        UserDto created = mapper.convertToDto(repository.save(mapper.convertToEntity(userDto)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Update current user", notes = "Accessible by users having one of the following roles: USER")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Successfully updated"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 422, message = "DTO validation failed")
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@ApiParam(value = "User DTO", required = true) @RequestBody @Valid UserDto userDto, @AuthenticationPrincipal User user) {
        repository.save(mapper.convertToEntity(userDto));
    }
}

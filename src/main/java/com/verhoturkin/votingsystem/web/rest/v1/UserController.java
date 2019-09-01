package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.repository.UserRepository;
import com.verhoturkin.votingsystem.to.UserDto;
import com.verhoturkin.votingsystem.util.exception.NotFoundException;
import com.verhoturkin.votingsystem.util.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

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

    // Admin part

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto created = mapper.convertToDto(repository.save(mapper.convertToEntity(userDto)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<UserDto> getAll() {
        List<User> users = repository.findAllByOrderByNameAscEmailAsc();
        return users.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable int id) {
        return mapper.convertToDto(repository.findById(id).orElseThrow(NotFoundException::new));
    }

    @GetMapping("/by")
    public UserDto getByEmail(@RequestParam String email) {
        return mapper.convertToDto(repository.findByEmail(email).orElseThrow(NotFoundException::new));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserDto userDto, @PathVariable int id) {
        repository.save(mapper.convertToEntity(userDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    // User part

    @GetMapping("/profile")
    public UserDto getProfile(@AuthenticationPrincipal User user) {
        return mapper.convertToDto(repository.findById(user.getId()).orElseThrow(NotFoundException::new));
    }

    @DeleteMapping("/profile")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User user) {
        repository.deleteById(user.getId());
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        UserDto created = mapper.convertToDto(repository.save(mapper.convertToEntity(userDto)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@RequestBody UserDto userDto, @AuthenticationPrincipal User user) {
        repository.save(mapper.convertToEntity(userDto));
    }
}

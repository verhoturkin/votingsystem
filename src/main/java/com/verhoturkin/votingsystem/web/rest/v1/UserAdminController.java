package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.Role;
import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.repository.UserRepository;
import com.verhoturkin.votingsystem.to.UserDto;
import com.verhoturkin.votingsystem.util.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@RestController
@RequestMapping(value = REST_V1 + "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAdminController {

    private final UserRepository repository;

    private final ModelMapper modelMapper;

    @Autowired
    public UserAdminController(UserRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        UserDto created = convertToDto(repository.save(convertToEntity(userDto)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/users/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<UserDto> getAll() {
        List<User> users = repository.findAllByOrderByNameAscEmailAsc();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable int id) {
        return convertToDto(repository.findById(id).orElseThrow(NotFoundException::new));
    }

    @GetMapping("/by")
    public UserDto getByEmail(@RequestParam String email) {
        return convertToDto(repository.findByEmail(email).orElseThrow(NotFoundException::new));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserDto userDto, @PathVariable int id) {
        repository.save(convertToEntity(userDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        repository.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User convertToEntity(UserDto postDto) throws ParseException {
        User user = modelMapper.map(postDto, User.class);

        if (!user.isNew()) {
            User oldUser = repository.findById(user.getId()).orElseThrow(NotFoundException::new);
            user.setRoles(oldUser.getRoles());
        } else {
            user.setRoles(Set.of(Role.ROLE_USER));
        }
        return user;
    }
}

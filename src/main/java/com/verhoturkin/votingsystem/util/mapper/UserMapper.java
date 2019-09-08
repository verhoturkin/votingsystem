package com.verhoturkin.votingsystem.util.mapper;

import com.verhoturkin.votingsystem.model.Role;
import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.repository.UserRepository;
import com.verhoturkin.votingsystem.to.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class UserMapper {

    private final ModelMapper mapper;

    private final PasswordEncoder encoder;

    private final UserRepository repository;

    @Autowired
    public UserMapper(ModelMapper mapper, PasswordEncoder encoder, UserRepository repository) {
        this.mapper = mapper;
        this.encoder = encoder;
        this.repository = repository;
    }

    public UserDto convertToDto(User user) {
        return Objects.isNull(user) ? null : mapper.map(user, UserDto.class);
    }

    public User convertToEntity(UserDto userDto) throws ParseException {
        User user = Objects.isNull(userDto) ? null : mapper.map(userDto, User.class);

        if (user.isNew()) {
            user.setRoles(Set.of(Role.ROLE_USER));
        } else {
            User oldUser = repository.findById(user.getId()).orElseThrow();
            user.setRoles(oldUser.getRoles());
            user.setRegistered(oldUser.getRegistered());
        }

        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}

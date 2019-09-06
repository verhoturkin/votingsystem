package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.model.Vote;
import com.verhoturkin.votingsystem.service.VoteService;
import com.verhoturkin.votingsystem.to.VoteDto;
import com.verhoturkin.votingsystem.util.mapper.VoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@RestController
@RequestMapping(value = REST_V1 + "/restaurants/{restaurantId}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    private final VoteMapper mapper;

    private final VoteService service;

    @Autowired
    public VoteController(VoteMapper mapper, VoteService service) {
        this.mapper = mapper;
        this.service = service;
    }

    //Admin Part
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<VoteDto> getAll(@PathVariable int restaurantId) {
        List<Vote> votes = service.findAllByRestaurantId(restaurantId);
        return votes.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/byDate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<VoteDto> getAllByDate(@PathVariable int restaurantId, @RequestParam LocalDate date) {
        List<Vote> votes = service.findAllByRestaurantIdAndDate(restaurantId, date);
        return votes.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    //User part

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<VoteDto> vote(@AuthenticationPrincipal User user, @PathVariable int restaurantId) {
        VoteDto created = mapper.convertToDto(service.save(user, restaurantId));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/restaurants/{restaurantId}/votes/{dishId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('ROLE_USER')")
    public VoteDto getCurrent(@AuthenticationPrincipal User user) {
        return mapper.convertToDto(service.get(user));
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ROLE_USER')")
    public long getCount(@PathVariable int restaurantId) {
        return service.getCount(restaurantId);
    }

}

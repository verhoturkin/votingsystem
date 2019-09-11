package com.verhoturkin.votingsystem.web.rest.v1;

import com.verhoturkin.votingsystem.model.User;
import com.verhoturkin.votingsystem.model.Vote;
import com.verhoturkin.votingsystem.service.VoteService;
import com.verhoturkin.votingsystem.to.VoteDto;
import com.verhoturkin.votingsystem.util.mapper.VoteMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.verhoturkin.votingsystem.config.WebConfig.REST_V1;

@Api(tags = "/votes", value = "/votes", description = "Operations with Votes", authorizations = {@Authorization(value = "basicAuth")})
@ApiResponses(value = {
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
})
@RestController
@RequestMapping(value = REST_V1 + "/votes", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteController {

    private final VoteMapper mapper;

    private final VoteService service;

    @Autowired
    public VoteController(VoteMapper mapper, VoteService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @ApiOperation(value = "Get all by restaurant ID", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = VoteDto.class, responseContainer = "List")})
    @GetMapping("/byRestaurant")
    public List<VoteDto> getByRestaurant(@ApiParam(value = "Restaurant Id which votes to get", required = true) @RequestParam int id) {
        List<Vote> votes = service.findAllByRestaurantId(id);
        return votes.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get all by date", notes = "Accessible by users having one of the following roles: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list", response = VoteDto.class, responseContainer = "List")})
    @GetMapping("/byDate")
    public List<VoteDto> getByDate(@ApiParam(value = "Date in ISO format ('YYYY-MM-DD')", required = true) @RequestParam LocalDate date) {
        List<Vote> votes = service.findAllByDate(date);
        return votes.stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Vote for restaurant", notes = "Accessible by users having one of the following roles: USER")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully create vote", response = VoteDto.class),
            @ApiResponse(code = 404, message = "Restaurant with given Id not found"),
            @ApiResponse(code = 409, message = "You can't vote again after 11.00"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteDto> vote(@AuthenticationPrincipal User user,
                                        @ApiParam(value = "Restaurant Id to vote for", required = true) @RequestParam int id) {
        VoteDto created = mapper.convertToDto(service.save(user, id));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_V1 + "/votes/{voteId}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @ApiOperation(value = "Get current user vote", notes = "Accessible by users having one of the following roles: USER")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = VoteDto.class),
            @ApiResponse(code = 404, message = "Current user's vote not found")})
    @GetMapping("/current")
    public VoteDto getCurrent(@AuthenticationPrincipal User user) {
        return mapper.convertToDto(service.get(user));
    }
}

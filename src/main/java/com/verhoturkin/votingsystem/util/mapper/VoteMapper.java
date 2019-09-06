package com.verhoturkin.votingsystem.util.mapper;

import com.verhoturkin.votingsystem.model.Vote;
import com.verhoturkin.votingsystem.to.VoteDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VoteMapper {

    private final ModelMapper mapper;

    public VoteMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public VoteDto convertToDto(Vote vote) {
        return Objects.isNull(vote) ? null : mapper.map(vote, VoteDto.class);
    }

    public Vote convertToEntity(VoteDto voteDto) {
        return Objects.isNull(voteDto) ? null : mapper.map(voteDto, Vote.class);
    }
}

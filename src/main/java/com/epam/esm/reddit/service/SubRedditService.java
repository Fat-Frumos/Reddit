package com.epam.esm.reddit.service;

import com.epam.esm.reddit.domain.SubReddit;
import com.epam.esm.reddit.dto.SubRedditDto;
import com.epam.esm.reddit.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class SubRedditService {

    private final SubRedditRepository subredditRepository;

    @Transactional
    public SubRedditDto save(SubRedditDto subRedditDto) {
        SubReddit save = subredditRepository.save(mapSubRedditDto(subRedditDto));
        subRedditDto.setId(save.getId());
        return subRedditDto;
    }

    @Transactional(readOnly = true)
    public List<SubRedditDto> getAll() {
        return subredditRepository
                .findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private SubRedditDto mapToDto(SubReddit subreddit) {
        return SubRedditDto
                .builder()
                .name(subreddit.getName())
                .id(subreddit.getId())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }

    private SubReddit mapSubRedditDto(SubRedditDto subRedditDto) {
        return SubReddit
                .builder()
                .name(subRedditDto.getName())
                .description(subRedditDto.getDescription())
                .build();
    }
}

package com.reddit.service;

import com.reddit.model.dto.SubRedditResponse;
import com.reddit.repository.SubRedditRepository;
import com.reddit.service.mapper.SubRedditMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
public class JpaSubRedditService implements SubRedditService {

    private final SubRedditMapper mapper;
    private final SubRedditRepository repository;

    @Transactional
    public ResponseEntity<SubRedditResponse> save(
            SubRedditResponse response) {
        response.setId(repository.save(mapper.toEntity(response)).getId());
        return ResponseEntity.status(CREATED).body(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<SubRedditResponse>> getAll() {
        return ResponseEntity.status(OK).body(repository
                .findAll()
                .stream()
                .map(mapper::toDto)
                .toList());
    }
}

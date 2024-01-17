package com.reddit.service;

import com.reddit.exception.SpringRedditException;
import com.reddit.model.dto.SubRedditResponse;
import com.reddit.model.entity.SubReddit;
import com.reddit.repository.SubRedditRepository;
import com.reddit.service.mapper.SubRedditMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Override
    public ResponseEntity<SubRedditResponse> getById(Long id) {
        SubReddit subReddit = repository
                .findById(id)
                .orElseThrow(() -> new SpringRedditException("Sub Reddit by Id not found"));
        return ResponseEntity.status(HttpStatus.OK)
                    .body(mapper.toDto(subReddit));
    }
}

package com.epam.esm.reddit.repository;

import com.epam.esm.reddit.domain.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {
}

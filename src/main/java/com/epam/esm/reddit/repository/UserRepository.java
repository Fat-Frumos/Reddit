package com.epam.esm.reddit.repository;

import com.epam.esm.reddit.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);
}

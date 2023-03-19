package com.epam.esm.reddit.repository;

import com.epam.esm.reddit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

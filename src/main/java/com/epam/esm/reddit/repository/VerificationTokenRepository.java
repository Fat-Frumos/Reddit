package com.epam.esm.reddit.repository;

import com.epam.esm.reddit.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository  extends JpaRepository<VerificationToken, Long> {
}

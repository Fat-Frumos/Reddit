package com.reddit.controller;

import com.reddit.repository.UserRepository;
import com.reddit.service.auth.SecurityUserDetailsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@TestConfiguration
public class TestConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new SecurityUserDetailsService(userRepository);
    }
}
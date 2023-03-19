package com.epam.esm.reddit.service;

import com.epam.esm.reddit.domain.Person;
import com.epam.esm.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

import static java.util.Collections.singletonList;

@AllArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Optional<Person> optionalPerson = userRepository.findByUsername(username);
        Person person = optionalPerson.orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
        return new org.springframework.security.core.userdetails.User(person.getUsername(),
                person.getPassword(), person.isEnabled(),
                true, true, true,
                getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return singletonList(new SimpleGrantedAuthority(role));
    }
}

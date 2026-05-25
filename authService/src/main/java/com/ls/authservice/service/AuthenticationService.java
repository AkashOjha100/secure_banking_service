package com.ls.authservice.service;

import com.ls.authservice.domain.User;
import com.ls.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username) {
        return userRepository.findByUsername(username)
                .filter(User::isEnabled)
                .orElseThrow(()-> new BadCredentialsException("Invalid user or disabled user"));
    }
}

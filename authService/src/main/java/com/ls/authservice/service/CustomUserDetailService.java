package com.ls.authservice.service;

import com.ls.authservice.domain.User;
import com.ls.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =userRepo.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .flatMap(role-> Stream.concat(Stream.of(new SimpleGrantedAuthority(role.getName())),role.getPermissions()
                                        .stream().map(permission -> new SimpleGrantedAuthority(permission.getName()))))
                                .collect(Collectors.toList())
        );
    }
}

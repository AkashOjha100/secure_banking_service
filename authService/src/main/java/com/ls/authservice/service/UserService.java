package com.ls.authservice.service;

import com.ls.authservice.domain.User;
import com.ls.authservice.dto.RegisterRequest;
import com.ls.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .branchId(request.getBranchId())
                .employeeCode(request.getEmployeeCode())
                .enabled(true)
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .build();
        return userRepo.save(user);
    }

    public User findByUserName(String username) {
        User user =userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException("Username not found"));
        return user;
    }
}

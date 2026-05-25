package com.ls.authservice.service;

import com.ls.authservice.audit.LoginAuditEntity;
import com.ls.authservice.repository.LoginAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    @Autowired
    private LoginAuditRepository auditRepo;

    public void logSuccess(String username, String ipAddress) {
        auditRepo.save(LoginAuditEntity.builder()
                        .username(username)
                        .success(true)
                        .ipAddress(ipAddress)
                        .loginTime(LocalDateTime.now())
                        .build());

    }

    public void logFailure(String username, String ipAddress, String reason) {
        auditRepo.save(LoginAuditEntity.builder()
                .username(username)
                .success(false)
                .ipAddress(ipAddress)
                .loginTime(LocalDateTime.now())
                .build());
    }
}

package com.ls.authservice.repository;

import com.ls.authservice.audit.LoginAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAuditRepository extends JpaRepository<LoginAuditEntity, Long> {
}

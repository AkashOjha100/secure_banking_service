package com.ls.authservice.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="login_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_name")
    private String username;
    private boolean success;
    @Column(name="ip_address")
    private String ipAddress;
    private String reason;
    @Column(name="login_time")
    private LocalDateTime loginTime;
}

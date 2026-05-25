package com.ls.authservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name ="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name="user_name")
    private String username;

    @Column(nullable = false)
    private String password;

    private boolean enabled = true;
    @Column(name="branch_id")
    private String branchId;
    @Column(name="employee_code")
    private String employeeCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name= "user_roles",joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles;
    private String email;
    private String phoneNumber;
    private boolean accountNonLocked =true;
    private int failedAttempts;
    private LocalDateTime lockTime;
}

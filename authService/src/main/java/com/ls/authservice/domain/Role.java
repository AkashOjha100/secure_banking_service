package com.ls.authservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name ="role_permission",
        joinColumns = @JoinColumn(name ="role_id"),
        inverseJoinColumns = @JoinColumn(name ="permission_id")
    )
    private Set<Permission> permissions;
}

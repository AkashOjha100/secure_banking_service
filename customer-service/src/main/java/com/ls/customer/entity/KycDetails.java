package com.ls.customer.entity;

import com.ls.customer.enums.KycStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="kyc_details")
public class KycDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String panNumber;
    private String aadhaarNumber;

    @Enumerated(EnumType.STRING)
    private KycStatus status;

    @ManyToOne
    @JoinColumn(name ="customer_id")
    private Customer customer;
}

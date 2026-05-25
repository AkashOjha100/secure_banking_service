package com.ls.customer.repository;

import com.ls.customer.entity.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycRepository extends JpaRepository<KycDetails,Long> {
    Optional<KycDetails> findByCustomerId(Long customerId);
}

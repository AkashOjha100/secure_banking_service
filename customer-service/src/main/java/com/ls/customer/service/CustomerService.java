package com.ls.customer.service;

import com.ls.customer.dto.CustomerRequest;
import com.ls.customer.dto.CustomerResponse;
import com.ls.customer.entity.Customer;
import com.ls.customer.enums.CustomerStatus;
import com.ls.customer.exception.ResourceNotFoundException;
import com.ls.customer.repository.CustomerRepository;
import com.ls.customer.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepo;

    public CustomerResponse createCustomer(CustomerRequest request) {
        Long userId = SecurityUtil.getUserId();
        customerRepo.findByUserIdAndIsDeletedFalse(userId).ifPresent(customer -> {
            throw new RuntimeException("Customer already exists");
        });
        Customer customer = Customer.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .userId(userId)
                .createdBy(SecurityUtil.getUsername())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(CustomerStatus.PENDING)
                .build();
         return mapToResponse(customerRepo.save(customer));
    }
    //find by ID
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepo.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Customer not found with id " + id));
        return mapToResponse(customer);
    }



    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .status(customer.getStatus())
                .build();
    }
}

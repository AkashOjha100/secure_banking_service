package com.ls.customer.dto;

import com.ls.customer.enums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private CustomerStatus status;
}

package com.ls.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "Phone Number required")
    private String phoneNumber;

    @Email(message = "Invalid email")
    private String email;

}

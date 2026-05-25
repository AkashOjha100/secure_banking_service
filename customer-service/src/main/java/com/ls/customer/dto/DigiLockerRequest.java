package com.ls.customer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DigiLockerRequest {
    private String redirectUrl;
}

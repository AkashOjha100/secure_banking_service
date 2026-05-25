package com.ls.customer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PanVerifyRequest {

    private String pan;
    private String consent;
    private String reason;
}

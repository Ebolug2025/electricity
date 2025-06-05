package com.etranzact.vasgate.electricity.kedc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationResponse {
    private Boolean success;
    private String message;
    private String err_message;
   private RequestPayload payload;

}

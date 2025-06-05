package com.etranzact.vasgate.electricity.kedc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestPayload {
    private Meter meter;
    private PostPaidAccount account;

}

package com.etranzact.vasgate.electricity.ekoedc.Domain.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String msgUser;
    private String msgDeveloper;
}

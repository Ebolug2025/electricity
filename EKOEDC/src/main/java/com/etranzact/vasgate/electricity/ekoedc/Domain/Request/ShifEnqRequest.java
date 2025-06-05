package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ShifEnqRequest {
    private String idVendor;
    private String codUser;
    private String codUserShift;
    private Long paymentDate;
}

package com.etranzact.vasgate.electricity.ekoedc.Domain.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ChngPassRequest {



    private String idVendor;
    private String codUser;
    private String currentPwd;
    private String newPwd;
}

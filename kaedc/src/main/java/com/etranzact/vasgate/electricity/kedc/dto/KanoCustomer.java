package com.etranzact.vasgate.electricity.kedc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanoCustomer {
    private String name;
    private String email;
    private String phone;
    private String gender;

}

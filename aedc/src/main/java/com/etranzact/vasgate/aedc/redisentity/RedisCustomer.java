package com.etranzact.vasgate.aedc.redisentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisCustomer {

    private String customer;
    private Integer id;

}

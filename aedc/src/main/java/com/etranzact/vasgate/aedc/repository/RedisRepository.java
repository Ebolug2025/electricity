package com.etranzact.vasgate.aedc.repository;

import com.etranzact.vasgate.aedc.redisentity.RedisCustomer;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisCustomer, Integer> {
}

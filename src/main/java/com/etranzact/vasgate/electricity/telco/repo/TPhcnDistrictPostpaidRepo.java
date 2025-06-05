package com.etranzact.vasgate.electricity.telco.repo;

import com.etranzact.vasgate.electricity.telco.entity.TPhcnDistrictPostpaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public interface TPhcnDistrictPostpaidRepo extends JpaRepository<TPhcnDistrictPostpaid, UUID> {
    @Query(value = "Select * from t_phcn_districts_postpaid  where disco = :disco and district LIKE :district",nativeQuery = true)
    Map<String,Object> getMerchantCode(String disco, String district);
}

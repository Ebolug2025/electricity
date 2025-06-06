package com.lemonpay.lemonpayvas.electricity.telco.repo;

import com.lemonpay.lemonpayvas.electricity.telco.entity.TPhcnDistrictPrepaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public interface TPhcnDistrictPrepaidRepo extends JpaRepository<TPhcnDistrictPrepaid, UUID> {
    @Query(value = "Select * from t_phcn_districts_prepaid  where disco = :disco and district LIKE :district",nativeQuery = true)
    Map<String,Object> getMerchantCode(String disco, String district);


}

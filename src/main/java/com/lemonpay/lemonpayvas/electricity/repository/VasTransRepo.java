package com.lemonpay.lemonpayvas.electricity.repository;

import com.lemonpay.lemonpayvas.electricity.entity.VasTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VasTransRepo extends JpaRepository<VasTrans,Long> {
    @Query(value = "Select * From vastrans where UNIQUE_TRANSID = :uniqueid",nativeQuery = true)
    Optional<VasTrans> findByUniqueTransId(String uniqueid);
}

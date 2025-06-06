package com.lemonpay.lemonpayvas.electricity.repository;

import com.lemonpay.lemonpayvas.electricity.entity.VasTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TPaytransRepo extends JpaRepository<VasTrans,Long> {
    @Query(value = "Select * From t_paytrans where UNIQUE_TRANSID = :uniqueid",nativeQuery = true)
    Optional<VasTrans> findByUniqueTransId(String uniqueid);
}

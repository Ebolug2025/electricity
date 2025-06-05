package com.etranzact.vasgate.electricity.repository;

import com.etranzact.vasgate.electricity.entity.TPaytrans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TPaytransRepo extends JpaRepository<TPaytrans,Long> {
    @Query(value = "Select * From t_paytrans where UNIQUE_TRANSID = :uniqueid",nativeQuery = true)
    Optional<TPaytrans> findByUniqueTransId(String uniqueid);
}

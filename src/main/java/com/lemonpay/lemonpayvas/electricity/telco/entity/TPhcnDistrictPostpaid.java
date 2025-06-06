package com.lemonpay.lemonpayvas.electricity.telco.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.util.UUID;
@Data
@Entity
@Table(name = "t_phcn_districts_postpaid")
public class TPhcnDistrictPostpaid {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "district_id", updatable = false, nullable = false)
  private UUID id;
  private String disco;
  private String district;
  private String payoutletMerchantCode;
  private String mobileMerchantCode;
  private String webMerchantCode;
  private String posMerchantCode;
  private String accountPrefix;
  private Date created;
  private String ussdMerchantCode;


}

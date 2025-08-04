package com.lemonpay.lemonpayvas.electricity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
@Table(name = "vastrans")
public class VasTrans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSID")
    private Long transid;
    @Column(name = "ACCOUNT")
    private String account;
    @Column(name = "ALIAS")
    private String alias;
    @Column(name = "TRANS_DATE")
    private Date transDate;
    @Column(name = "TRANS_PERIOD")
    private String transPeriod;
    @Column(name = "TRANS_TYPE")
    private String transType;
    @Column(name = "TRANS_CHANNEL")
    private String transChannel;
    @Column(name = "TRANS_AMOUNT")
    private double transAmount;
    @Column(name = "TRANS_STATUS")
    private String transStatus;
    @Column(name = "TRANS_NOTE")
    private String transNote;
    @Column(name = "MOBILE_NO")
    private String mobileNo;
    @Column(name = "TRANS_NO")
    private String transNo;
    @Column(name = "SUB_CODE")
    private String subCode;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "T_FULLNAME")
    private String tFullname;
    @Column(name = "T_ADDRESS")
    private String tAddress;
    @Column(name = "UNIQUE_TRANSID")
    private String uniqueTransid;
    @Column(name = "STATUS_DESCRIPTION")
    private String statusDescription;
    @Column(name = "RESPONSE_DATE")
    private Date responseDate;
//just to push
}

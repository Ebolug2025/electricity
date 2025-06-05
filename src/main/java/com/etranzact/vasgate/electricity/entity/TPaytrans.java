package com.etranzact.vasgate.electricity.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;


@Data
@Entity
@Table(name = "t_paytrans")
public class TPaytrans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSID")
    private Long transid;
    @Column(name = "merchant_code")
    private String merchantId;
    @Column(name = "MERCHANT_CODE", columnDefinition = "varchar(20)")
    private String merchantCode;
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
    @Column(name = "SUBSCRIBER_ID")
    private String subscriberId;
    @Column(name = "MOBILE_NO")
    private String mobileNo;
    @Column(name = "CARD_SUBNAME")
    private String cardSubname;
    @Column(name = "CARD_FULLNAME")
    private String cardFullname;
    @Column(name = "CARD_ACCOUNT")
    private String cardAccount;
    @Column(name = "CARD_NUM")
    private String cardNum;
    @Column(name = "CARD_ISSUERSUBCODES")
    private String cardIssuersubcodes;
    @Column(name = "SPNF_BATCHNO")
    private String spnfBatchno;
    @Column(name = "USER_NAME")
    private String username;
    @Column(name = "SP_STATUS")
    private String spStatus;
    @Column(name = "TRANS_NO")
    private String transNo;
    @Column(name = "ISSUER_CODE")
    private String issuerCode;
    @Column(name = "SUB_CODE")
    private String subCode;
    @Column(name = "payment_type")
    private String paymentType;
    @Column(name = "T_FULLNAME")
    private String tFullname;
    @Column(name = "T_ADDRESS")
    private String tAddress;
    @Column(name = "T_QUANTITY")
    private BigInteger tQuantity;
    @Column(name = "PAYMENT_CODE")
    private String paymentCode;
    @Column(name = "CHEQUE_NO")
    private String chequeNo;
    @Column(name = "CHEQUE_BANK")
    private String chequeBank;
    @Column(name = "AUT_USERNAME")
    private String authUsername;
    @Column(name = "UNIQUE_TRANSID")
    private String uniqueTransid;
    @Column(name = "INT_STATUS")
    private String intStatus;
    @Column(name = "PROCESS_STATUS")
    private  String processStatus;
    @Column(name = "STATUS_DESCRIPTION")
    private String statusDescription;
    @Column(name = "RESPONSE_DATE")
    private Date responseDate;
//just to push
}

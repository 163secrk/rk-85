package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contractNo;

    private Long subscriptionId;

    private String subscriptionNo;

    private Long customerId;

    private String customerName;

    private String customerPhone;

    private String customerIdCard;

    private String customerAddress;

    private String sellerName;

    private String sellerPhone;

    private String sellerIdCard;

    private String sellerAddress;

    private Long propertyId;

    private String propertyTitle;

    private String propertyAddress;

    private String propertyNo;

    private String propertyType;

    private BigDecimal area;

    private Integer rooms;

    private Integer halls;

    private Integer bathrooms;

    private String floor;

    private String orientation;

    private String decoration;

    private BigDecimal totalPrice;

    private BigDecimal unitPrice;

    private BigDecimal depositAmount;

    private BigDecimal firstPayment;

    private BigDecimal remainingAmount;

    private String paymentMethod;

    private Integer paymentPeriod;

    private LocalDateTime signDate;

    private LocalDateTime deliveryDate;

    private LocalDateTime recordDate;

    private String status;

    private String agentName;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (contractNo == null) {
            contractNo = "HT" + System.currentTimeMillis();
        }
        if (status == null) {
            status = "草稿";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}

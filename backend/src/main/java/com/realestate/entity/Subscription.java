package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String subscriptionNo;

    private Long customerId;

    private String customerName;

    private String customerPhone;

    private String customerIdCard;

    private Long propertyId;

    private String propertyTitle;

    private String propertyAddress;

    private BigDecimal totalPrice;

    private BigDecimal unitPrice;

    private BigDecimal area;

    private BigDecimal depositAmount;

    private BigDecimal firstPayment;

    private BigDecimal remainingAmount;

    private String paymentMethod;

    private Integer paymentPeriod;

    private LocalDateTime signDate;

    private LocalDateTime deliveryDate;

    private String status;

    private String agentName;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (subscriptionNo == null) {
            subscriptionNo = "SUB" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}

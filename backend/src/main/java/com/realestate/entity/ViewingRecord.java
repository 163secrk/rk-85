package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "viewing_record")
public class ViewingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long customerId;

    private String customerName;

    private String customerPhone;

    private Long propertyId;

    private String propertyTitle;

    private String propertyAddress;

    private String agentName;

    private LocalDateTime viewingTime;

    private Integer viewingDuration;

    private String satisfaction;

    private String customerFeedback;

    private String followUpPlan;

    private String status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}

package com.realestate.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "follow_up_record")
public class FollowUpRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "客户不能为空")
    private Long customerId;

    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    @NotBlank(message = "跟进方式不能为空")
    private String followType;

    @NotBlank(message = "跟进内容不能为空")
    private String followContent;

    private String followResult;

    @NotNull(message = "跟进时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime followTime;

    private String nextFollowPlan;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextFollowTime;

    @NotBlank(message = "经纪人不能为空")
    private String agentName;

    @NotBlank(message = "状态不能为空")
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

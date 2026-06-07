package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "performance_assessment")
public class PerformanceAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long agentId;

    private String agentName;

    @NotBlank(message = "统计月份不能为空")
    private String month;

    private Integer viewingCount;

    private Integer dealCount;

    private BigDecimal dealAmount;

    private Integer totalCustomers;

    private Integer dealCustomers;

    private BigDecimal conversionRate;

    @DecimalMin(value = "0", message = "分数不能小于0")
    @DecimalMax(value = "100", message = "分数不能大于100")
    private BigDecimal score;

    private String grade;

    private String evaluator;

    private String evaluationRemark;

    private LocalDateTime evaluationTime;

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

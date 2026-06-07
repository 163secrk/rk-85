package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "monthly_target")
public class MonthlyTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long agentId;

    private String agentName;

    @NotBlank(message = "统计月份不能为空")
    private String month;

    @NotNull(message = "带看量目标不能为空")
    @Min(value = 0, message = "带看量目标不能小于0")
    private Integer viewingTarget;

    @NotNull(message = "成交量目标不能为空")
    @Min(value = 0, message = "成交量目标不能小于0")
    private Integer dealTarget;

    @NotNull(message = "成交总额目标不能为空")
    @DecimalMin(value = "0", message = "成交总额目标不能小于0")
    private BigDecimal dealAmountTarget;

    @NotNull(message = "客户转化率目标不能为空")
    @DecimalMin(value = "0", message = "客户转化率目标不能小于0")
    @DecimalMax(value = "100", message = "客户转化率目标不能大于100")
    private BigDecimal conversionRateTarget;

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

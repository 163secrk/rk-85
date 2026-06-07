package com.realestate.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PerformanceStatsDTO {
    private Long agentId;
    private String agentName;
    private String month;
    private Integer viewingCount;
    private Integer viewingTarget;
    private BigDecimal viewingCompletionRate;
    private Integer dealCount;
    private Integer dealTarget;
    private BigDecimal dealCompletionRate;
    private BigDecimal dealAmount;
    private BigDecimal dealAmountTarget;
    private BigDecimal dealAmountCompletionRate;
    private Integer totalCustomers;
    private Integer dealCustomers;
    private BigDecimal conversionRate;
    private BigDecimal conversionRateTarget;
    private BigDecimal conversionCompletionRate;
    private BigDecimal score;
    private String grade;
    private String evaluationRemark;
    private Integer rank;
}

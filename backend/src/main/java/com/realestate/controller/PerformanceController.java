package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.dto.PerformanceStatsDTO;
import com.realestate.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/performance")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @GetMapping("/monthly/{month}")
    public Result<List<PerformanceStatsDTO>> getMonthlyPerformance(@PathVariable String month) {
        return Result.success(performanceService.calculateMonthlyPerformance(month));
    }

    @GetMapping("/agent/{agentId}/month/{month}")
    public Result<PerformanceStatsDTO> getAgentPerformance(@PathVariable Long agentId,
                                                           @PathVariable String month) {
        PerformanceStatsDTO dto = performanceService.getAgentPerformance(agentId, month);
        if (dto == null) {
            return Result.error("未找到该经纪人的业绩数据");
        }
        return Result.success(dto);
    }

    @GetMapping("/ranking/chart/{month}")
    public Result<Map<String, Object>> getRankingChartData(@PathVariable String month) {
        return Result.success(performanceService.getRankingChartData(month));
    }

    @GetMapping("/radar/chart/{month}")
    public Result<Map<String, Object>> getRadarChartData(@PathVariable String month) {
        return Result.success(performanceService.getRadarChartData(month));
    }
}

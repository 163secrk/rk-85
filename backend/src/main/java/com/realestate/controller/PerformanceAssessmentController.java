package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.PerformanceAssessment;
import com.realestate.service.PerformanceAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/performance-assessments")
public class PerformanceAssessmentController {

    @Autowired
    private PerformanceAssessmentService assessmentService;

    @GetMapping
    public Result<List<PerformanceAssessment>> list() {
        return Result.success(assessmentService.findAll());
    }

    @GetMapping("/{id}")
    public Result<PerformanceAssessment> getById(@PathVariable Long id) {
        Optional<PerformanceAssessment> assessment = assessmentService.findById(id);
        return assessment.map(Result::success).orElseGet(() -> Result.error("记录不存在"));
    }

    @GetMapping("/month/{month}")
    public Result<List<PerformanceAssessment>> getByMonth(@PathVariable String month) {
        return Result.success(assessmentService.findByMonth(month));
    }

    @GetMapping("/month/{month}/ranking")
    public Result<List<PerformanceAssessment>> getByMonthRanking(@PathVariable String month) {
        return Result.success(assessmentService.findByMonthOrderByDealAmountDesc(month));
    }

    @GetMapping("/agent/{agentId}")
    public Result<List<PerformanceAssessment>> getByAgentId(@PathVariable Long agentId) {
        return Result.success(assessmentService.findByAgentId(agentId));
    }

    @GetMapping("/agent/{agentId}/month/{month}")
    public Result<PerformanceAssessment> getByAgentIdAndMonth(@PathVariable Long agentId, @PathVariable String month) {
        Optional<PerformanceAssessment> assessment = assessmentService.findByAgentIdAndMonth(agentId, month);
        return assessment.map(Result::success).orElseGet(() -> Result.success(null));
    }

    @PostMapping
    public Result<PerformanceAssessment> add(@RequestBody PerformanceAssessment assessment) {
        return Result.success("添加成功", assessmentService.save(assessment));
    }

    @PutMapping
    public Result<PerformanceAssessment> update(@RequestBody PerformanceAssessment assessment) {
        return Result.success("更新成功", assessmentService.update(assessment));
    }

    @PostMapping("/evaluate")
    public Result<PerformanceAssessment> evaluate(@RequestParam Long id,
                                                  @RequestParam BigDecimal score,
                                                  @RequestParam String grade,
                                                  @RequestParam(required = false) String evaluator,
                                                  @RequestParam(required = false) String evaluationRemark) {
        try {
            PerformanceAssessment result = assessmentService.evaluate(id, score, grade, evaluator, evaluationRemark);
            return Result.success("评分成功", result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        assessmentService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

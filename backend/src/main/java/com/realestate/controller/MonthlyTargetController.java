package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.MonthlyTarget;
import com.realestate.service.MonthlyTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/monthly-targets")
public class MonthlyTargetController {

    @Autowired
    private MonthlyTargetService monthlyTargetService;

    @GetMapping
    public Result<List<MonthlyTarget>> list() {
        return Result.success(monthlyTargetService.findAll());
    }

    @GetMapping("/{id}")
    public Result<MonthlyTarget> getById(@PathVariable Long id) {
        Optional<MonthlyTarget> target = monthlyTargetService.findById(id);
        return target.map(Result::success).orElseGet(() -> Result.error("目标不存在"));
    }

    @GetMapping("/month/{month}")
    public Result<List<MonthlyTarget>> getByMonth(@PathVariable String month) {
        return Result.success(monthlyTargetService.findByMonth(month));
    }

    @GetMapping("/agent/{agentId}")
    public Result<List<MonthlyTarget>> getByAgentId(@PathVariable Long agentId) {
        return Result.success(monthlyTargetService.findByAgentId(agentId));
    }

    @GetMapping("/agent/{agentId}/month/{month}")
    public Result<MonthlyTarget> getByAgentIdAndMonth(@PathVariable Long agentId, @PathVariable String month) {
        Optional<MonthlyTarget> target = monthlyTargetService.findByAgentIdAndMonth(agentId, month);
        return target.map(Result::success).orElseGet(() -> Result.success(null));
    }

    @PostMapping
    public Result<MonthlyTarget> add(@Valid @RequestBody MonthlyTarget target) {
        try {
            return Result.success("设置成功", monthlyTargetService.save(target));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping
    public Result<MonthlyTarget> update(@Valid @RequestBody MonthlyTarget target) {
        return Result.success("更新成功", monthlyTargetService.update(target));
    }

    @PostMapping("/batch")
    public Result<Void> batchSave(@RequestBody List<MonthlyTarget> targets) {
        monthlyTargetService.batchSave(targets);
        return Result.success("批量设置成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        monthlyTargetService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

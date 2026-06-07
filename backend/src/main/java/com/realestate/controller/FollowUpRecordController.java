package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.FollowUpRecord;
import com.realestate.service.FollowUpRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/follow-up-records")
public class FollowUpRecordController {

    @Autowired
    private FollowUpRecordService followUpRecordService;

    @GetMapping
    public Result<List<FollowUpRecord>> list() {
        return Result.success(followUpRecordService.findAll());
    }

    @GetMapping("/{id}")
    public Result<FollowUpRecord> getById(@PathVariable Long id) {
        Optional<FollowUpRecord> record = followUpRecordService.findById(id);
        return record.map(Result::success).orElseGet(() -> Result.error("跟进记录不存在"));
    }

    @GetMapping("/customer/{customerId}")
    public Result<List<FollowUpRecord>> getByCustomerId(@PathVariable Long customerId) {
        return Result.success(followUpRecordService.findByCustomerId(customerId));
    }

    @GetMapping("/search")
    public Result<List<FollowUpRecord>> search(@RequestParam(required = false) Long customerId,
                                               @RequestParam(required = false) String agentName,
                                               @RequestParam(required = false) String followType,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        agentName = (agentName != null && agentName.trim().isEmpty()) ? null : agentName;
        followType = (followType != null && followType.trim().isEmpty()) ? null : followType;
        return Result.success(followUpRecordService.search(customerId, agentName, followType, startTime, endTime));
    }

    @PostMapping
    public Result<FollowUpRecord> add(@Valid @RequestBody FollowUpRecord followUpRecord) {
        return Result.success("添加成功", followUpRecordService.save(followUpRecord));
    }

    @PutMapping
    public Result<FollowUpRecord> update(@Valid @RequestBody FollowUpRecord followUpRecord) {
        return Result.success("更新成功", followUpRecordService.update(followUpRecord));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        followUpRecordService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

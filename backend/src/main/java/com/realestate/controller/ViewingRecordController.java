package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.ViewingRecord;
import com.realestate.service.ViewingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/viewing-records")
public class ViewingRecordController {

    @Autowired
    private ViewingRecordService viewingRecordService;

    @GetMapping
    public Result<List<ViewingRecord>> list() {
        return Result.success(viewingRecordService.findAll());
    }

    @GetMapping("/{id}")
    public Result<ViewingRecord> getById(@PathVariable Long id) {
        Optional<ViewingRecord> record = viewingRecordService.findById(id);
        return record.map(Result::success).orElseGet(() -> Result.error("带看记录不存在"));
    }

    @GetMapping("/customer/{customerId}")
    public Result<List<ViewingRecord>> getByCustomerId(@PathVariable Long customerId) {
        return Result.success(viewingRecordService.findByCustomerId(customerId));
    }

    @GetMapping("/property/{propertyId}")
    public Result<List<ViewingRecord>> getByPropertyId(@PathVariable Long propertyId) {
        return Result.success(viewingRecordService.findByPropertyId(propertyId));
    }

    @GetMapping("/search")
    public Result<List<ViewingRecord>> search(@RequestParam(required = false) Long customerId,
                                              @RequestParam(required = false) Long propertyId,
                                              @RequestParam(required = false) String agentName,
                                              @RequestParam(required = false) String status,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
                                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(viewingRecordService.search(customerId, propertyId, agentName, status, startTime, endTime));
    }

    @PostMapping
    public Result<ViewingRecord> add(@RequestBody ViewingRecord viewingRecord) {
        return Result.success("添加成功", viewingRecordService.save(viewingRecord));
    }

    @PutMapping
    public Result<ViewingRecord> update(@RequestBody ViewingRecord viewingRecord) {
        return Result.success("更新成功", viewingRecordService.update(viewingRecord));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        viewingRecordService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.Subscription;
import com.realestate.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public Result<List<Subscription>> list() {
        return Result.success(subscriptionService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Subscription> getById(@PathVariable Long id) {
        Optional<Subscription> subscription = subscriptionService.findById(id);
        return subscription.map(Result::success).orElseGet(() -> Result.error("认购记录不存在"));
    }

    @GetMapping("/no/{subscriptionNo}")
    public Result<Subscription> getBySubscriptionNo(@PathVariable String subscriptionNo) {
        return Result.success(subscriptionService.findBySubscriptionNo(subscriptionNo));
    }

    @GetMapping("/customer/{customerId}")
    public Result<List<Subscription>> getByCustomerId(@PathVariable Long customerId) {
        return Result.success(subscriptionService.findByCustomerId(customerId));
    }

    @GetMapping("/property/{propertyId}")
    public Result<List<Subscription>> getByPropertyId(@PathVariable Long propertyId) {
        return Result.success(subscriptionService.findByPropertyId(propertyId));
    }

    @GetMapping("/search")
    public Result<List<Subscription>> search(@RequestParam(required = false) Long customerId,
                                             @RequestParam(required = false) Long propertyId,
                                             @RequestParam(required = false) String status,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        status = (status != null && status.trim().isEmpty()) ? null : status;
        return Result.success(subscriptionService.search(customerId, propertyId, status, startDate, endDate));
    }

    @PostMapping
    public Result<Subscription> add(@RequestBody Subscription subscription) {
        return Result.success("添加成功", subscriptionService.save(subscription));
    }

    @PutMapping
    public Result<Subscription> update(@RequestBody Subscription subscription) {
        return Result.success("更新成功", subscriptionService.update(subscription));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        subscriptionService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

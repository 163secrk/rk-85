package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.service.CustomerService;
import com.realestate.service.PropertyService;
import com.realestate.service.SubscriptionService;
import com.realestate.service.ViewingRecordService;
import com.realestate.service.VRPanoramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ViewingRecordService viewingRecordService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private VRPanoramaService vrPanoramaService;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customerService.findAll().size());
        stats.put("totalProperties", propertyService.findAll().size());
        stats.put("totalViewingRecords", viewingRecordService.findAll().size());
        stats.put("totalSubscriptions", subscriptionService.findAll().size());
        stats.put("totalVRPanoramas", vrPanoramaService.findAll().size());
        stats.put("pendingSubscriptions", subscriptionService.findByStatus("待签约").size());
        stats.put("completedSubscriptions", subscriptionService.findByStatus("已签约").size());
        stats.put("activeCustomers", customerService.findByStatus("跟进中").size());
        return Result.success(stats);
    }
}

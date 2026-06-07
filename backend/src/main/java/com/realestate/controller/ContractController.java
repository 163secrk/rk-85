package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.Contract;
import com.realestate.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping
    public Result<List<Contract>> list() {
        return Result.success(contractService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Contract> getById(@PathVariable Long id) {
        Optional<Contract> contract = contractService.findById(id);
        return contract.map(Result::success).orElseGet(() -> Result.error("合同不存在"));
    }

    @GetMapping("/no/{contractNo}")
    public Result<Contract> getByContractNo(@PathVariable String contractNo) {
        return Result.success(contractService.findByContractNo(contractNo));
    }

    @GetMapping("/customer/{customerId}")
    public Result<List<Contract>> getByCustomerId(@PathVariable Long customerId) {
        return Result.success(contractService.findByCustomerId(customerId));
    }

    @GetMapping("/property/{propertyId}")
    public Result<List<Contract>> getByPropertyId(@PathVariable Long propertyId) {
        return Result.success(contractService.findByPropertyId(propertyId));
    }

    @GetMapping("/search")
    public Result<List<Contract>> search(@RequestParam(required = false) Long customerId,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        status = (status != null && status.trim().isEmpty()) ? null : status;
        return Result.success(contractService.search(customerId, status, startDate, endDate));
    }

    @PostMapping
    public Result<Contract> add(@RequestBody Contract contract) {
        return Result.success("添加成功", contractService.save(contract));
    }

    @PutMapping
    public Result<Contract> update(@RequestBody Contract contract) {
        return Result.success("更新成功", contractService.update(contract));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        contractService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/create-from-subscription/{subscriptionId}")
    public Result<Contract> createFromSubscription(@PathVariable Long subscriptionId) {
        try {
            Contract contract = contractService.createFromSubscription(subscriptionId);
            return Result.success("合同创建成功", contract);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<Contract> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Contract contract = contractService.updateStatus(id, status);
            return Result.success("状态更新成功", contract);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }
}

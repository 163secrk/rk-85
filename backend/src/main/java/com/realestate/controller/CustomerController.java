package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.Customer;
import com.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Result<List<Customer>> list() {
        return Result.success(customerService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        return customer.map(Result::success).orElseGet(() -> Result.error("客户不存在"));
    }

    @GetMapping("/search")
    public Result<List<Customer>> search(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String phone,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) String level) {
        name = (name != null && name.trim().isEmpty()) ? null : name;
        phone = (phone != null && phone.trim().isEmpty()) ? null : phone;
        status = (status != null && status.trim().isEmpty()) ? null : status;
        level = (level != null && level.trim().isEmpty()) ? null : level;
        return Result.success(customerService.search(name, phone, status, level));
    }

    @PostMapping
    public Result<Customer> add(@Valid @RequestBody Customer customer) {
        return Result.success("添加成功", customerService.save(customer));
    }

    @PutMapping
    public Result<Customer> update(@Valid @RequestBody Customer customer) {
        return Result.success("更新成功", customerService.update(customer));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

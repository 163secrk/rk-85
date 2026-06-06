package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.Property;
import com.realestate.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @GetMapping
    public Result<List<Property>> list() {
        return Result.success(propertyService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Property> getById(@PathVariable Long id) {
        Optional<Property> property = propertyService.findById(id);
        return property.map(Result::success).orElseGet(() -> Result.error("房源不存在"));
    }

    @GetMapping("/search")
    public Result<List<Property>> search(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) String type,
                                         @RequestParam(required = false) String city,
                                         @RequestParam(required = false) String district,
                                         @RequestParam(required = false) BigDecimal minPrice,
                                         @RequestParam(required = false) BigDecimal maxPrice) {
        return Result.success(propertyService.search(title, status, type, city, district, minPrice, maxPrice));
    }

    @PostMapping
    public Result<Property> add(@Valid @RequestBody Property property) {
        return Result.success("添加成功", propertyService.save(property));
    }

    @PutMapping
    public Result<Property> update(@Valid @RequestBody Property property) {
        return Result.success("更新成功", propertyService.update(property));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        propertyService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

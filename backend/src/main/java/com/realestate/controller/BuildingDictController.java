package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.dto.BuildingDictDetailDTO;
import com.realestate.entity.BuildingDict;
import com.realestate.service.BuildingDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/building-dicts")
public class BuildingDictController {

    @Autowired
    private BuildingDictService buildingDictService;

    @GetMapping
    public Result<List<BuildingDict>> list() {
        return Result.success(buildingDictService.findAll());
    }

    @GetMapping("/{id}")
    public Result<BuildingDict> getById(@PathVariable Long id) {
        Optional<BuildingDict> buildingDict = buildingDictService.findById(id);
        return buildingDict.map(Result::success).orElseGet(() -> Result.error("楼盘不存在"));
    }

    @GetMapping("/{id}/detail")
    public Result<BuildingDictDetailDTO> getDetailById(@PathVariable Long id) {
        Optional<BuildingDictDetailDTO> detail = buildingDictService.getBuildingDictDetail(id);
        return detail.map(Result::success).orElseGet(() -> Result.error("楼盘不存在"));
    }

    @GetMapping("/search")
    public Result<List<BuildingDict>> search(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String city,
                                             @RequestParam(required = false) String district) {
        return Result.success(buildingDictService.search(name, city, district));
    }

    @PostMapping
    public Result<BuildingDict> add(@Valid @RequestBody BuildingDict buildingDict) {
        return Result.success("添加成功", buildingDictService.save(buildingDict));
    }

    @PutMapping
    public Result<BuildingDict> update(@Valid @RequestBody BuildingDict buildingDict) {
        return Result.success("更新成功", buildingDictService.update(buildingDict));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        buildingDictService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

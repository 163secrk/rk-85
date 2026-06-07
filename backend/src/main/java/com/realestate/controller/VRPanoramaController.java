package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.VRPanorama;
import com.realestate.service.VRPanoramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vr-panoramas")
public class VRPanoramaController {

    @Autowired
    private VRPanoramaService vrPanoramaService;

    @GetMapping
    public Result<List<VRPanorama>> list() {
        return Result.success(vrPanoramaService.findAll());
    }

    @GetMapping("/{id}")
    public Result<VRPanorama> getById(@PathVariable Long id) {
        Optional<VRPanorama> vr = vrPanoramaService.findById(id);
        return vr.map(Result::success).orElseGet(() -> Result.error("VR全景不存在"));
    }

    @GetMapping("/property/{propertyId}")
    public Result<List<VRPanorama>> getByPropertyId(@PathVariable Long propertyId) {
        return Result.success(vrPanoramaService.findByPropertyId(propertyId));
    }

    @GetMapping("/search")
    public Result<List<VRPanorama>> search(@RequestParam(required = false) Long propertyId,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) String status) {
        name = (name != null && name.trim().isEmpty()) ? null : name;
        status = (status != null && status.trim().isEmpty()) ? null : status;
        return Result.success(vrPanoramaService.search(propertyId, name, status));
    }

    @PostMapping
    public Result<VRPanorama> add(@RequestBody VRPanorama vrPanorama) {
        return Result.success("添加成功", vrPanoramaService.save(vrPanorama));
    }

    @PutMapping
    public Result<VRPanorama> update(@RequestBody VRPanorama vrPanorama) {
        return Result.success("更新成功", vrPanoramaService.update(vrPanorama));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        vrPanoramaService.deleteById(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/{id}/view")
    public Result<VRPanorama> incrementViewCount(@PathVariable Long id) {
        VRPanorama vr = vrPanoramaService.incrementViewCount(id);
        if (vr != null) {
            return Result.success(vr);
        }
        return Result.error("VR全景不存在");
    }
}

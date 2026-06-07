package com.realestate.controller;

import com.realestate.common.Result;
import com.realestate.entity.Agent;
import com.realestate.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping
    public Result<List<Agent>> list() {
        return Result.success(agentService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Agent> getById(@PathVariable Long id) {
        Optional<Agent> agent = agentService.findById(id);
        return agent.map(Result::success).orElseGet(() -> Result.error("经纪人不存在"));
    }

    @GetMapping("/search")
    public Result<List<Agent>> search(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String status) {
        name = (name != null && name.trim().isEmpty()) ? null : name;
        status = (status != null && status.trim().isEmpty()) ? null : status;
        return Result.success(agentService.search(name, status));
    }

    @GetMapping("/names")
    public Result<List<String>> getActiveAgentNames() {
        return Result.success(agentService.findAllActiveAgentNames());
    }

    @PostMapping
    public Result<Agent> add(@Valid @RequestBody Agent agent) {
        Optional<Agent> existing = agentService.findByPhone(agent.getPhone());
        if (existing.isPresent()) {
            return Result.error("该手机号已存在");
        }
        return Result.success("添加成功", agentService.save(agent));
    }

    @PutMapping
    public Result<Agent> update(@Valid @RequestBody Agent agent) {
        return Result.success("更新成功", agentService.update(agent));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        agentService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

package com.realestate.service;

import com.realestate.entity.MonthlyTarget;
import com.realestate.repository.MonthlyTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonthlyTargetService {

    @Autowired
    private MonthlyTargetRepository monthlyTargetRepository;

    public List<MonthlyTarget> findAll() {
        return monthlyTargetRepository.findAll();
    }

    public Optional<MonthlyTarget> findById(Long id) {
        return monthlyTargetRepository.findById(id);
    }

    public List<MonthlyTarget> findByMonth(String month) {
        return monthlyTargetRepository.findByMonth(month);
    }

    public Optional<MonthlyTarget> findByAgentIdAndMonth(Long agentId, String month) {
        return monthlyTargetRepository.findByAgentIdAndMonth(agentId, month);
    }

    public List<MonthlyTarget> findByAgentId(Long agentId) {
        return monthlyTargetRepository.findByAgentId(agentId);
    }

    public MonthlyTarget save(MonthlyTarget target) {
        boolean exists = monthlyTargetRepository.existsByAgentIdAndMonth(
                target.getAgentId(), target.getMonth());
        if (exists && target.getId() == null) {
            throw new RuntimeException("该经纪人本月目标已存在，请勿重复设置");
        }
        return monthlyTargetRepository.save(target);
    }

    public MonthlyTarget update(MonthlyTarget target) {
        return monthlyTargetRepository.save(target);
    }

    public void deleteById(Long id) {
        monthlyTargetRepository.deleteById(id);
    }

    public void batchSave(List<MonthlyTarget> targets) {
        for (MonthlyTarget target : targets) {
            Optional<MonthlyTarget> existing = monthlyTargetRepository
                    .findByAgentIdAndMonth(target.getAgentId(), target.getMonth());
            if (existing.isPresent()) {
                MonthlyTarget e = existing.get();
                e.setViewingTarget(target.getViewingTarget());
                e.setDealTarget(target.getDealTarget());
                e.setDealAmountTarget(target.getDealAmountTarget());
                e.setConversionRateTarget(target.getConversionRateTarget());
                monthlyTargetRepository.save(e);
            } else {
                monthlyTargetRepository.save(target);
            }
        }
    }
}

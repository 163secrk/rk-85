package com.realestate.service;

import com.realestate.entity.PerformanceAssessment;
import com.realestate.repository.PerformanceAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PerformanceAssessmentService {

    @Autowired
    private PerformanceAssessmentRepository assessmentRepository;

    public List<PerformanceAssessment> findAll() {
        return assessmentRepository.findAll();
    }

    public Optional<PerformanceAssessment> findById(Long id) {
        return assessmentRepository.findById(id);
    }

    public List<PerformanceAssessment> findByMonth(String month) {
        return assessmentRepository.findByMonth(month);
    }

    public List<PerformanceAssessment> findByMonthOrderByDealAmountDesc(String month) {
        return assessmentRepository.findByMonthOrderByDealAmountDesc(month);
    }

    public Optional<PerformanceAssessment> findByAgentIdAndMonth(Long agentId, String month) {
        return assessmentRepository.findByAgentIdAndMonth(agentId, month);
    }

    public List<PerformanceAssessment> findByAgentId(Long agentId) {
        return assessmentRepository.findByAgentId(agentId);
    }

    public PerformanceAssessment save(PerformanceAssessment assessment) {
        return assessmentRepository.save(assessment);
    }

    public PerformanceAssessment update(PerformanceAssessment assessment) {
        return assessmentRepository.save(assessment);
    }

    public void deleteById(Long id) {
        assessmentRepository.deleteById(id);
    }

    public PerformanceAssessment evaluate(Long id, BigDecimal score, String grade,
                                          String evaluator, String evaluationRemark) {
        Optional<PerformanceAssessment> opt = assessmentRepository.findById(id);
        if (!opt.isPresent()) {
            throw new RuntimeException("业绩记录不存在");
        }
        PerformanceAssessment assessment = opt.get();
        assessment.setScore(score);
        assessment.setGrade(grade);
        assessment.setEvaluator(evaluator);
        assessment.setEvaluationRemark(evaluationRemark);
        assessment.setEvaluationTime(LocalDateTime.now());
        return assessmentRepository.save(assessment);
    }

    public PerformanceAssessment findOrCreateByAgentIdAndMonth(Long agentId, String agentName, String month) {
        Optional<PerformanceAssessment> opt = assessmentRepository.findByAgentIdAndMonth(agentId, month);
        if (opt.isPresent()) {
            return opt.get();
        }
        PerformanceAssessment assessment = new PerformanceAssessment();
        assessment.setAgentId(agentId);
        assessment.setAgentName(agentName);
        assessment.setMonth(month);
        assessment.setViewingCount(0);
        assessment.setDealCount(0);
        assessment.setDealAmount(BigDecimal.ZERO);
        assessment.setTotalCustomers(0);
        assessment.setDealCustomers(0);
        assessment.setConversionRate(BigDecimal.ZERO);
        return assessmentRepository.save(assessment);
    }
}

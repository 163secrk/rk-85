package com.realestate.repository;

import com.realestate.entity.PerformanceAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceAssessmentRepository extends JpaRepository<PerformanceAssessment, Long> {
    List<PerformanceAssessment> findByMonth(String month);

    Optional<PerformanceAssessment> findByAgentIdAndMonth(Long agentId, String month);

    List<PerformanceAssessment> findByAgentId(Long agentId);

    @Query("SELECT p FROM PerformanceAssessment p WHERE p.month = ?1 ORDER BY p.dealAmount DESC NULLS LAST")
    List<PerformanceAssessment> findByMonthOrderByDealAmountDesc(String month);

    boolean existsByAgentIdAndMonth(Long agentId, String month);
}

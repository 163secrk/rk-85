package com.realestate.repository;

import com.realestate.entity.MonthlyTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyTargetRepository extends JpaRepository<MonthlyTarget, Long> {
    List<MonthlyTarget> findByMonth(String month);

    Optional<MonthlyTarget> findByAgentIdAndMonth(Long agentId, String month);

    List<MonthlyTarget> findByAgentId(Long agentId);

    boolean existsByAgentIdAndMonth(Long agentId, String month);
}

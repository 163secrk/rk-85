package com.realestate.repository;

import com.realestate.entity.FollowUpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FollowUpRecordRepository extends JpaRepository<FollowUpRecord, Long> {

    List<FollowUpRecord> findByCustomerIdOrderByFollowTimeDesc(Long customerId);

    List<FollowUpRecord> findByAgentName(String agentName);

    @Query("SELECT f FROM FollowUpRecord f WHERE (:customerId IS NULL OR f.customerId = :customerId) " +
           "AND (:agentName IS NULL OR f.agentName LIKE %:agentName%) " +
           "AND (:followType IS NULL OR f.followType = :followType) " +
           "AND (:startTime IS NULL OR f.followTime >= :startTime) " +
           "AND (:endTime IS NULL OR f.followTime <= :endTime)")
    List<FollowUpRecord> findByConditions(@Param("customerId") Long customerId,
                                          @Param("agentName") String agentName,
                                          @Param("followType") String followType,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
}

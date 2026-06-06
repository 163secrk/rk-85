package com.realestate.repository;

import com.realestate.entity.ViewingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViewingRecordRepository extends JpaRepository<ViewingRecord, Long> {

    List<ViewingRecord> findByCustomerId(Long customerId);

    List<ViewingRecord> findByPropertyId(Long propertyId);

    List<ViewingRecord> findByAgentName(String agentName);

    List<ViewingRecord> findByStatus(String status);

    @Query("SELECT v FROM ViewingRecord v WHERE (:customerId IS NULL OR v.customerId = :customerId) " +
           "AND (:propertyId IS NULL OR v.propertyId = :propertyId) " +
           "AND (:agentName IS NULL OR v.agentName LIKE %:agentName%) " +
           "AND (:status IS NULL OR v.status = :status) " +
           "AND (:startTime IS NULL OR v.viewingTime >= :startTime) " +
           "AND (:endTime IS NULL OR v.viewingTime <= :endTime)")
    List<ViewingRecord> findByConditions(@Param("customerId") Long customerId,
                                         @Param("propertyId") Long propertyId,
                                         @Param("agentName") String agentName,
                                         @Param("status") String status,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);
}

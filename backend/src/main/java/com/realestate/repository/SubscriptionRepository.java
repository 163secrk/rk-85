package com.realestate.repository;

import com.realestate.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByCustomerId(Long customerId);

    List<Subscription> findByPropertyId(Long propertyId);

    List<Subscription> findByStatus(String status);

    Subscription findBySubscriptionNo(String subscriptionNo);

    @Query("SELECT s FROM Subscription s WHERE (:customerId IS NULL OR s.customerId = :customerId) " +
           "AND (:propertyId IS NULL OR s.propertyId = :propertyId) " +
           "AND (:status IS NULL OR s.status = :status) " +
           "AND (:startDate IS NULL OR s.signDate >= :startDate) " +
           "AND (:endDate IS NULL OR s.signDate <= :endDate)")
    List<Subscription> findByConditions(@Param("customerId") Long customerId,
                                        @Param("propertyId") Long propertyId,
                                        @Param("status") String status,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
}

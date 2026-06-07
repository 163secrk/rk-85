package com.realestate.repository;

import com.realestate.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByCustomerId(Long customerId);

    List<Contract> findByPropertyId(Long propertyId);

    List<Contract> findByStatus(String status);

    Contract findByContractNo(String contractNo);

    Contract findBySubscriptionId(Long subscriptionId);

    @Query("SELECT c FROM Contract c WHERE (:customerId IS NULL OR c.customerId = :customerId) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "AND (:startDate IS NULL OR c.createTime >= :startDate) " +
           "AND (:endDate IS NULL OR c.createTime <= :endDate)")
    List<Contract> findByConditions(@Param("customerId") Long customerId,
                                    @Param("status") String status,
                                    @Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
}

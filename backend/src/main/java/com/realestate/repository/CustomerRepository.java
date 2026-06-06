package com.realestate.repository;

import com.realestate.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContainingOrPhoneContaining(String name, String phone);

    List<Customer> findByStatus(String status);

    List<Customer> findByLevel(String level);

    @Query("SELECT c FROM Customer c WHERE (:name IS NULL OR c.name LIKE %:name%) " +
           "AND (:phone IS NULL OR c.phone LIKE %:phone%) " +
           "AND (:status IS NULL OR c.status = :status) " +
           "AND (:level IS NULL OR c.level = :level)")
    List<Customer> findByConditions(@Param("name") String name,
                                    @Param("phone") String phone,
                                    @Param("status") String status,
                                    @Param("level") String level);
}

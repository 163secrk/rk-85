package com.realestate.repository;

import com.realestate.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByTitleContaining(String title);

    List<Property> findByStatus(String status);

    List<Property> findByType(String type);

    List<Property> findByCity(String city);

    @Query("SELECT p FROM Property p WHERE (:title IS NULL OR p.title LIKE %:title%) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:type IS NULL OR p.type = :type) " +
           "AND (:city IS NULL OR p.city = :city) " +
           "AND (:district IS NULL OR p.district = :district) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Property> findByConditions(@Param("title") String title,
                                    @Param("status") String status,
                                    @Param("type") String type,
                                    @Param("city") String city,
                                    @Param("district") String district,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice);
}

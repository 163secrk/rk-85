package com.realestate.repository;

import com.realestate.entity.VRPanorama;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VRPanoramaRepository extends JpaRepository<VRPanorama, Long> {

    List<VRPanorama> findByPropertyId(Long propertyId);

    List<VRPanorama> findByPropertyIdOrderBySortOrderAsc(Long propertyId);

    List<VRPanorama> findByStatus(String status);

    @Query("SELECT v FROM VRPanorama v WHERE (:propertyId IS NULL OR v.propertyId = :propertyId) " +
           "AND (:name IS NULL OR v.name LIKE %:name%) " +
           "AND (:status IS NULL OR v.status = :status)")
    List<VRPanorama> findByConditions(@Param("propertyId") Long propertyId,
                                      @Param("name") String name,
                                      @Param("status") String status);
}

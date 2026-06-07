package com.realestate.repository;

import com.realestate.entity.BuildingDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingDictRepository extends JpaRepository<BuildingDict, Long> {

    List<BuildingDict> findByNameContaining(String name);

    List<BuildingDict> findByCity(String city);

    List<BuildingDict> findByDistrict(String district);

    @Query("SELECT b FROM BuildingDict b WHERE (:name IS NULL OR b.name LIKE %:name%) " +
           "AND (:city IS NULL OR b.city = :city) " +
           "AND (:district IS NULL OR b.district = :district)")
    List<BuildingDict> findByConditions(@Param("name") String name,
                                        @Param("city") String city,
                                        @Param("district") String district);
}

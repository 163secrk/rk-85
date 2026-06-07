package com.realestate.service;

import com.realestate.dto.BuildingDictDetailDTO;
import com.realestate.entity.BuildingDict;
import com.realestate.entity.Property;
import com.realestate.repository.BuildingDictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuildingDictService {

    @Autowired
    private BuildingDictRepository buildingDictRepository;

    @Autowired
    private PropertyService propertyService;

    public List<BuildingDict> findAll() {
        return buildingDictRepository.findAll();
    }

    public Optional<BuildingDict> findById(Long id) {
        return buildingDictRepository.findById(id);
    }

    public List<BuildingDict> search(String name, String city, String district) {
        name = (name != null && name.trim().isEmpty()) ? null : name.trim();
        city = (city != null && city.trim().isEmpty()) ? null : city.trim();
        district = (district != null && district.trim().isEmpty()) ? null : district.trim();
        return buildingDictRepository.findByConditions(name, city, district);
    }

    public BuildingDict save(BuildingDict buildingDict) {
        return buildingDictRepository.save(buildingDict);
    }

    public BuildingDict update(BuildingDict buildingDict) {
        return buildingDictRepository.save(buildingDict);
    }

    public void deleteById(Long id) {
        buildingDictRepository.deleteById(id);
    }

    public List<BuildingDict> findByNameContaining(String name) {
        return buildingDictRepository.findByNameContaining(name);
    }

    public Optional<BuildingDictDetailDTO> getBuildingDictDetail(Long id) {
        Optional<BuildingDict> buildingDictOpt = buildingDictRepository.findById(id);
        if (!buildingDictOpt.isPresent()) {
            return Optional.empty();
        }

        BuildingDict buildingDict = buildingDictOpt.get();
        BuildingDictDetailDTO detailDTO = new BuildingDictDetailDTO();
        detailDTO.setBuildingDict(buildingDict);

        List<Property> onSaleProperties = propertyService.findByBuildingDictIdAndStatus(id, "在售");
        List<Property> soldProperties = propertyService.findByBuildingDictIdAndStatus(id, "已售");
        detailDTO.setOnSaleProperties(onSaleProperties);
        detailDTO.setSoldProperties(soldProperties);

        BigDecimal historicalAvg = propertyService.calculateHistoricalAveragePrice(id);
        BigDecimal currentAvg = propertyService.calculateCurrentAveragePrice(id);
        detailDTO.setHistoricalAveragePrice(historicalAvg);
        detailDTO.setCurrentAveragePrice(currentAvg);

        Long onSaleCount = propertyService.countByBuildingDictIdAndStatus(id, "在售");
        Long soldCount = propertyService.countByBuildingDictIdAndStatus(id, "已售");
        detailDTO.setOnSaleCount(onSaleCount);
        detailDTO.setSoldCount(soldCount);

        if (buildingDict.getSupportingFacilities() != null && !buildingDict.getSupportingFacilities().isEmpty()) {
            List<String> facilities = Arrays.stream(buildingDict.getSupportingFacilities().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            detailDTO.setSupportingFacilityList(facilities);
        }

        return Optional.of(detailDTO);
    }
}

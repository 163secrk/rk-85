package com.realestate.dto;

import com.realestate.entity.BuildingDict;
import com.realestate.entity.Property;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BuildingDictDetailDTO {
    private BuildingDict buildingDict;
    private List<Property> onSaleProperties;
    private List<Property> soldProperties;
    private BigDecimal historicalAveragePrice;
    private BigDecimal currentAveragePrice;
    private Long onSaleCount;
    private Long soldCount;
    private List<String> supportingFacilityList;
}

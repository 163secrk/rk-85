package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "building_dict")
public class BuildingDict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "楼盘名称不能为空")
    private String name;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "区域不能为空")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    private String address;

    private String buildingType;

    private String buildingYear;

    private String developer;

    private String propertyCompany;

    private String totalHouses;

    private String plotRatio;

    private String greeningRate;

    private String parkingSpace;

    private String supportingFacilities;

    private String description;

    private String imageUrl;

    private String averagePrice;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}

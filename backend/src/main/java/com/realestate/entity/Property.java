package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String propertyNo;

    private String type;

    private String area;

    private String address;

    private String city;

    private String district;

    private BigDecimal price;

    private BigDecimal unitPrice;

    private Integer rooms;

    private Integer halls;

    private Integer bathrooms;

    private String floor;

    private String totalFloor;

    private String orientation;

    private String decoration;

    private String buildingType;

    private String buildingYear;

    private String description;

    private String features;

    private String supportingFacilities;

    private String status;

    private String imageUrl;

    private String images;

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

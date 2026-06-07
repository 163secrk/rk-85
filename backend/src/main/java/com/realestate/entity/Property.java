package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "房源名称不能为空")
    private String title;

    @NotBlank(message = "房源编号不能为空")
    private String propertyNo;

    @NotBlank(message = "房源类型不能为空")
    private String type;

    @NotBlank(message = "面积不能为空")
    private String area;

    @NotBlank(message = "详细地址不能为空")
    private String address;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "区域不能为空")
    private String district;

    @NotNull(message = "总价不能为空")
    @DecimalMin(value = "0", message = "总价不能小于0")
    private BigDecimal price;

    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0", message = "单价不能小于0")
    private BigDecimal unitPrice;

    @NotNull(message = "室数不能为空")
    @Min(value = 1, message = "室数不能小于1")
    private Integer rooms;

    @NotNull(message = "厅数不能为空")
    @Min(value = 0, message = "厅数不能小于0")
    private Integer halls;

    @NotNull(message = "卫数不能为空")
    @Min(value = 0, message = "卫数不能小于0")
    private Integer bathrooms;

    @NotBlank(message = "楼层不能为空")
    private String floor;

    private String totalFloor;

    @NotBlank(message = "朝向不能为空")
    private String orientation;

    @NotBlank(message = "装修不能为空")
    private String decoration;

    private String buildingType;

    private String buildingYear;

    private String description;

    private String features;

    private String supportingFacilities;

    @NotBlank(message = "状态不能为空")
    private String status;

    private Long buildingDictId;

    private String buildingName;

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

package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vr_panorama")
public class VRPanorama {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long propertyId;

    private String propertyTitle;

    private String name;

    private String description;

    private String thumbnailUrl;

    private String panoramaUrl;

    private String panoramaList;

    private String initialView;

    private String hotspots;

    private Integer viewCount;

    private Integer sortOrder;

    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (viewCount == null) {
            viewCount = 0;
        }
        if (sortOrder == null) {
            sortOrder = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}

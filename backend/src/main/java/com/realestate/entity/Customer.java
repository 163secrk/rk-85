package com.realestate.entity;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "性别不能为空")
    private String gender;

    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄不能小于1岁")
    @Max(value = 120, message = "年龄不能大于120岁")
    private Integer age;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", message = "身份证号格式不正确")
    private String idCard;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotBlank(message = "购房预算不能为空")
    private String budget;

    @NotBlank(message = "意向区域不能为空")
    private String preferredArea;

    @NotBlank(message = "意向户型不能为空")
    private String preferredType;

    @NotBlank(message = "客户状态不能为空")
    private String status;

    @NotBlank(message = "客户来源不能为空")
    private String source;

    @NotBlank(message = "客户等级不能为空")
    private String level;

    private String remark;

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

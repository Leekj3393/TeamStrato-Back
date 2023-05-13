package com.strato.skylift.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CSequipmentCatgoryDTO
{
    private Long categoryCode;
    private String categoryName;
    private EquiCategoryDTO equCategory;
    private Long categoryCount;
    private Date equipmentCreateDate;
    private Date equipmentModifyDate;
}

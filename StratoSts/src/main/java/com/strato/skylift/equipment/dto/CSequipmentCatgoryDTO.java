package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CSequipmentCatgoryDTO
{
    private Long categoryCode;
    private String categoryName;
    private EquiCategoryDTO equCategory;
    private Long categoryCount;
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
    private Date equipmentCreateDate;
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
    private Date  equipmentModifyDate;
}

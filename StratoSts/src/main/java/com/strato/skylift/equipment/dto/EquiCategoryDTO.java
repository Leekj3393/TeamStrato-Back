package com.strato.skylift.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EquiCategoryDTO
{
    private Long categoryCode;
    private String categoryName;
    private String refCategoryCode;
}

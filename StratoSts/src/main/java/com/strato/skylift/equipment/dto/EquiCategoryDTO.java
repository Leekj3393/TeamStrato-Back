package com.strato.skylift.equipment.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class EquiCategoryDTO
{
    private Long categoryCode;
    private String categoryName;
    private EquiCategoryDTO equCategory;
}

package com.strato.skylift.equipment.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EquipmentDTO
{
    private Long EquipmentCode;
    private EquiCategoryDTO equCategory;
    private String equipmentName;
    private Date equipmentCreateDate;
    private Date equipmentModifyDate;
    private String equipmentStatus;
    private List<EQFileDTO> files = new ArrayList<>();
}

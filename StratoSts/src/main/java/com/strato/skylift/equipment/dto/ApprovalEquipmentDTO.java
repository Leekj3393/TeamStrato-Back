package com.strato.skylift.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalEquipmentDTO
{
    private Long equipmentCode;
    private String appTitle;
    private String appContent;
    private String appType;
}

package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
    private Date equipmentCreateDate;
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
    private Date equipmentModifyDate;
    private String equipmentStatus;
    private List<EQFileDTO> files = new ArrayList<>();

    @JsonIgnore
    private MultipartFile equipmentImage;
}

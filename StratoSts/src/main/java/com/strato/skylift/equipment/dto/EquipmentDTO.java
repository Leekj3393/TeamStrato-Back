package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strato.skylift.entity.EquCategory;
import com.strato.skylift.equipment.entity.EquipmentFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class EquipmentDTO
{
    private Long equipmentCode;
    private EquiCategoryDTO equCategory;
    private String equipmentName;
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
    private Date equipmentCreateDate;
    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm")
    private Date equipmentModifyDate;
    private String equipmentStatus;
    private EQFileDTO file;

    @JsonIgnore
    private MultipartFile equipmentImage;
}

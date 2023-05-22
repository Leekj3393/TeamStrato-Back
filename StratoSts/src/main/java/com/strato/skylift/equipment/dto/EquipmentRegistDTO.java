package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class EquipmentRegistDTO
{
    private String equipmentName;

    private EquiCategoryDTO equCategory;

    private String appTitle;

    private String appContent;

    private String appType;

    @JsonIgnore
    private MultipartFile image;

    private EQFileDTO file;

}

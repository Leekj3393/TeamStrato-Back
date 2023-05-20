package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EquipmentRegistDTO
{
    private String equipmentName;

    private EquiCategoryDTO equCategory;

    private String appTitle;

    private String appContent;

    @JsonIgnore
    private MultipartFile image;

    private List<EQFileDTO> files = new ArrayList<>();

}

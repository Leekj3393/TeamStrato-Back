package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EQFileDTO
{
    private Long fileCode;
    private String fileName;
    private String filePath;
    private String fileType;

    @JsonIgnore
    private MultipartFile EquipmentImage;

    public EQFileDTO(String imageName, String reFileName, String 장비) {
    }
}

package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class EQFileDTO
{
    private Long fileCode;
    private String fileName;
    private String filePath;
    private String fileType;

    @JsonIgnore
    private MultipartFile EquipmentImage;

    public EQFileDTO(String imageName, String reFileName, String fileType)
    {
        this.fileName = imageName;
        this.filePath = reFileName;
        this.fileType = fileType;
    }
}

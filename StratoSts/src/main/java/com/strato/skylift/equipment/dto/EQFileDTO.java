package com.strato.skylift.equipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class EQFileDTO
{
    public EQFileDTO(String fileName , String filePath , String fileType)
    {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }
    private Long fileCode;
    private String fileName;
    private String filePath;
    private String fileType;

    @JsonIgnore
    private MultipartFile equipmentImage;
}

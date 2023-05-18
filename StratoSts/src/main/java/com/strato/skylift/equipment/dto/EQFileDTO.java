package com.strato.skylift.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EQFileDTO
{
    private Long fileCode;
    private String fileName;
    private String filePath;
    private String fileType;
}

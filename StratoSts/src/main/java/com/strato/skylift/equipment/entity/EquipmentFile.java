package com.strato.skylift.equipment.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_FILE")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor

@SequenceGenerator(name="FILE_SEQ_GENERATOR",
        sequenceName="SEQ_FILE",
        initialValue=1, allocationSize=1)
public class EquipmentFile
{
    @Id
    @Column(name="FILE_CODE")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FILE_SEQ_GENERATOR")
    private Long fileCode;

    @Column(name="FILE_NAME")
    private String fileName;

    @Column(name="FILE_PATH")
    private String filePath;

    @Column(name="FILE_TYPE")
    private String fileType;

    @Column(name = "EQUIPMENT_CODE")
    private Long equipmentCode;


}

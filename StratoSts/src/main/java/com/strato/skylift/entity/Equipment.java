package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TBL_EQUIPMENT")
@SequenceGenerator(name = "EQUIPMENT_SEQ_GENERATOR",
                  sequenceName = "SEQ_EQUIPMENT",
                initialValue = 1 , allocationSize = 0)
@Getter @Setter @DynamicInsert @ToString
public class Equipment
{
    @Id @Column(name = "EQUIPMENT_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EQUIPMENT_SEQ_GENERATOR")
    private Long equipmentCode;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_CODE")
    private EquCategory equCategory;

    @Column(name = "EQUIPMENT_NAME")
    private String equipmentName;

    @Column(name = "EQUIPMENT_CREATE_DATE")
    private Date equipmentCreateDate;

    @Column(name = "EQUIPMENT_MODIFY_DATE")
    private Date equipmentModifyDate;

    @Column(name = "EQUIPMENT_STATUS")
    private String equipmentStatus;

    @OneToMany
    @JoinColumn(name = "EQUIPMENT_CODE")
    private List<File> files = new ArrayList<>();


}

package com.strato.skylift.equipment.entity;

import com.strato.skylift.entity.Member;
import com.strato.skylift.entity.Request;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;



@Entity
@Table(name = "TBL_APPROVAL")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @DynamicInsert @ToString
@SequenceGenerator(name = "APPROVAL_SEQ_GENERATOR",
        sequenceName = "SEQ_APPROVAL",
        initialValue = 1 , allocationSize = 0)
public class EquipmentApproval
{
    @Id
    @Column(name = "APP_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APPROVAL_SEQ_GENERATOR")
    private Long appCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="EQUIPMENT_CODE")
    private EquipmentRegist equipmentCode;

    @Column(name = "APP_TITLE")
    private String appTitle;

    @Column(name = "APP_CONTENT")
    private String appContent;

    @Column(name = "APP_TYPE")
    private String appType;

    @Column(name = "APP_STATUS")
    private String appStatus;

    @Column(name = "APP_REGIST_DATE")
    private Date appRegistDate;

    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "APP_WDL_DATE")
    private Date appWdlDate;
}

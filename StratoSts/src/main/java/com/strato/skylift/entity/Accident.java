package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TBL_ACCIDENT")
@Getter @Setter @DynamicInsert
@SequenceGenerator(name = "ACCIDENT_SEQ_GENERATOR",
                   sequenceName = "SEQ_ACCIDENT",
                    initialValue = 1 , allocationSize = 0)
public class Accident
{
    @Id @Column(name = "ACCIDENT_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCIDENT_SEQ_GENERATOR")
    private Long accidentCode;

    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @Column(name = "ACCIDENT_NAME")
    private String accidentName;

    @Column(name = "ACCIDENT_DES")
    private String accidentDes;

    @Column(name = "ACCIDENT_LOCATION")
    private String accidentLocation;

    @Column(name = "ACCIDENT_TIME")
    private String accidentTime;

    @Column(name = "ACCIDENT_MODIFY_TIME")
    private Date accidentModifyTime;

    @Column(name = "ACCIDENT_DELETE_TIME")
    private Date accidentDeleteTime;

    @Column(name = "ACCIDENT_STATUS")
    private String accidentStatus;

    @Column(name = "ACCIDENT_CRATE_DATE")
    private Date accidentCrateDate;





}

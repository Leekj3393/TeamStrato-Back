package com.strato.skylift.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_MEMBER_ROLE")
@Getter @Setter
public class MemberRole
{
    @Id @Column(name = "ROLE_CODE")
    private Long roleCode;

    @Column(name = "ROLE_NAME")
    private String roleName;
    
    @Column(name= "ROLE_DES")
    private String roleDes;
    
}

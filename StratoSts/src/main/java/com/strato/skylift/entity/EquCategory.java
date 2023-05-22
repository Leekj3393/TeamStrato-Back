package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_EQU_CATEGORY")
@Getter @Setter
public class EquCategory
{
    @Id @Column(name = "CATEGORY_CODE")
    private Long categoryCode;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "REF_CATEGORY_CODE")
    private EquCategory equCategory;

    @OneToMany(mappedBy = "equCategory")
    private List<EquCategory> child = new ArrayList<>();

}

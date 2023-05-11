package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TBL_SCH_CATEGORY")
@Getter @Setter
public class SchCategory
{
    @Id @Column(name = "CATEGORY_NO")
    private Long categoryNo;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @Column(name = "CATEGORY_COLOR")
    private String categoryColor;

    @ManyToOne
    @JoinColumn(name = "REF_CATEGORY_NO")
    private SchCategory schCategory;

    @OneToMany(mappedBy = "schCategory")
    private List<SchCategory> child = new ArrayList<>();

}

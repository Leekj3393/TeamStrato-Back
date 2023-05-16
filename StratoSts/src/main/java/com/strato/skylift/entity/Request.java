package com.strato.skylift.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TBL_REQUEST")
@SequenceGenerator(name = "REQUEST_SEQ_GENERATOR",
                    sequenceName = "SEQ_REQUEST",
                    initialValue = 1 , allocationSize = 0)
@Getter @Setter
public class Request
{
    @Id @Column(name = "REQUEST_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "REQUEST_SEQ_GENERATOR")
    private Long requestCode;

    @ManyToOne
    @JoinColumn(name = "MEMBER_CODE")
    private Member member;

    @Column(name = "REQUEST_REASON")
    private String requestReason;

    @Column(name = "REQUSET_TYPE")
    private String requsetType;

    @Column(name = "REQUEST_START")
    private Date requestStart;

    @Column(name = "REQUEST_END")
    private Date requestEnd;

    @OneToMany
    @JoinColumn(name = "request")
    private List<Approval> approvals = new ArrayList<>();
}

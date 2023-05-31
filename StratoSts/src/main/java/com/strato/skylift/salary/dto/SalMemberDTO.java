package com.strato.skylift.salary.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strato.skylift.member.dto.MbDepartmentDto;
import com.strato.skylift.member.dto.MbFileDto;
import com.strato.skylift.member.dto.MbJobDto;
import com.strato.skylift.member.dto.MbMemberRoleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SalMemberDTO
{
    private Long memberCode;

    private String memberId;

    private String memberPwd;

    private String memberName;

    private String residentNo;

    private String gender;

    private String phone;

    private String address;

    private String memberStatus;

    private Date memberHireDate;

    private String bankName;

    private String bankNo;

    private Long memberSalary;

    private Long memberAnnual;

    private MbJobDto job;

    private MbDepartmentDto department;

    private MbMemberRoleDto memberRole;

    private List<MbFileDto> files;

    private String updatePwd;

    @JsonIgnore
    private MultipartFile memberImage;
}

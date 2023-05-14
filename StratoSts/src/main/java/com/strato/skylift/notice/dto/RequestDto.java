package com.strato.skylift.notice.dto;

import com.strato.skylift.entity.Member;
import com.strato.skylift.member.dto.MbMemberDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class RequestDto {
    private Long requestCode;
    private Long member;
    private String requestReason;
    private String requsetType;

    //리퀘스트 쓰시는 분들은 연락주세요 ! 제가 제 로직때문에 String 으로 해놨어요
    private String requestStart;
    private String requestEnd;
}


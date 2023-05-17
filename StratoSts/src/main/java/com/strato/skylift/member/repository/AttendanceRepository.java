package com.strato.skylift.member.repository;

import com.strato.skylift.entity.Attendance;
import com.strato.skylift.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    //근태관리
    Optional<Attendance> findAllByMemberMemberCode(Long memberCode);

    Optional<Attendance> findByMember(Member member);


    //List<Attendance> findByMemberIn(Long member);

//
//
//    List<Attendance> findAllByMember(Member member);
}

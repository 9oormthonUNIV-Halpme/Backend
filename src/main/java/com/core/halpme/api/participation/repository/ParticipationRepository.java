package com.core.halpme.api.participation.repository;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.participation.entity.Participation;

import java.util.List;

public interface ParticipationRepository extends org.springframework.data.jpa.repository.JpaRepository<Participation, Long> {
    //내가 신청한 게시글 목록조회
    List<Participation> findByMember(Member member);
    //특정 게시글에 신청한 봉사자 조회
    List<Participation> findByPost_Id(Long postId);
}

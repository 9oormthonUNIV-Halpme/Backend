package com.core.halpme.api.mypage.service;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.api.mypage.dto.MyPageResponseDto;
import com.core.halpme.api.mypage.dto.MyParticipationDto;
import com.core.halpme.api.mypage.dto.MyPostDto;
import com.core.halpme.api.participation.repository.ParticipationRepository;
import com.core.halpme.api.post.repository.PostRepository;
import com.core.halpme.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ParticipationRepository participationRepository;

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPage(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

        List<MyPostDto> posts = member.getPosts().stream()
                .map(post -> MyPostDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .build())
                .collect(Collectors.toList());

        // 내가 신청한 봉사
        List<MyParticipationDto> participations = participationRepository.findByMember(member).stream()
                .map(p -> MyParticipationDto.builder()
                        .postId(p.getPost().getId())
                        .title(p.getPost().getTitle())
                        .city(p.getPost().getAddress().getCity())
                        .dong(p.getPost().getAddress().getDong())
                        .status(p.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        return MyPageResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .age(member.getAge())
                .gender(member.getGender().name())
                .posts(posts)
                .participations(participations)
                .build();

    }


}

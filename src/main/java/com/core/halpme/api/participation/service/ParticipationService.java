package com.core.halpme.api.participation.service;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.api.participation.dto.ParticipationResponseDto;
import com.core.halpme.api.participation.entity.Participation;
import com.core.halpme.api.participation.repository.ParticipationRepository;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.repository.PostRepository;
import com.core.halpme.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    //봉사 신청하기
    @Transactional
    public void applyToPost(String email, Long postId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("회원 없음"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글 없음"));

        Participation participation = Participation.of(post, member);
        participationRepository.save(participation);
    }

    //내가 신청한 봉사 확인
    @Transactional(readOnly = true)
    public List<ParticipationResponseDto> getMyApplications(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("회원 없음"));

        List<Participation> list = participationRepository.findByMember(member);

        return list.stream()
                .map(ParticipationResponseDto::new)
                .collect(Collectors.toList());
    }
}

package com.core.halpme.api.rank.service;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.entity.PostStatus;
import com.core.halpme.api.rank.entity.Rank;
import com.core.halpme.api.rank.repository.RankRepository;
import com.core.halpme.common.exception.NotFoundException;
import com.core.halpme.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {
    private final RankRepository rankRepository;

    //봉사 기록 갱신
    @Transactional
    public void updateRank(Member volunteer, Post post, int hours) {
        if(post.getPostStatus() != PostStatus.AUTHENTICATED) {
            throw new IllegalStateException("AUTHENTICATED 상태의 글만 봉사시간으로 인정합니다.");
        }

        Rank rank = rankRepository.findByMemberEmail(volunteer.getEmail())
                .orElseThrow(()-> new NotFoundException(ErrorStatus.NOT_FOUND_USER.getMessage()));

        rank.updateVolunteerRecord(hours);
        rankRepository.save(rank);
    }

    //유저 총 봉사시간 조회
    @Transactional(readOnly = true)
    public int getMyTotalVolunteerHours(String email) {
        return rankRepository.findByMemberEmail(email)
                .map(Rank::getTotalVolunteerHours)
                .orElse(0);
    }

    //랭킹 조회
    @Transactional(readOnly = true)
    public List<Rank> getToRanks() {
        return rankRepository.findAll()
                .stream()

                //큰값이 먼저 오도록
                .sorted((a,b) -> Integer.compare(b.getTotalVolunteerHours(), a.getTotalVolunteerHours()))
                //상위 10명
                .limit(10)
                .toList();
    }



}

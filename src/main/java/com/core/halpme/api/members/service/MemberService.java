package com.core.halpme.api.members.service;

import com.core.halpme.api.members.dto.LoginRequestDto;
import com.core.halpme.api.members.dto.MemberInfoResponseDto;
import com.core.halpme.api.members.dto.SignupRequestDto;
import com.core.halpme.api.members.dto.UpdateMemberRequestDto;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.jwt.JwtTokenProvider;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.api.post.repository.PostRepository;
import com.core.halpme.api.rank.entity.Rank;
import com.core.halpme.api.rank.repository.RankRepository;
import com.core.halpme.common.exception.BaseException;
import com.core.halpme.common.exception.NotFoundException;
import com.core.halpme.common.exception.UnauthorizedException;
import com.core.halpme.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RankRepository rankRepository;

    @Transactional
    public void signupMember(SignupRequestDto request) {

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getMessage());
        }

        if (memberRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BaseException(HttpStatus.BAD_REQUEST, ErrorStatus.BAD_REQUEST_DUPLICATE_PHONE.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Member member = request.toEntity(encodedPassword);
        
        // Member 저장 - id 생성
        memberRepository.save(member);

        // Rank 생성 및 Member 와 매핑
        Rank rank = new Rank(member);

        // Rank 저장
        rankRepository.save(rank);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> loginMember(LoginRequestDto request) {

        // id = email, pw = password

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_OR_PASSWORD.getMessage()));

        if (!passwordEncoder.matches(request.getPw(), member.getPassword())) {
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_EMAIL_OR_PASSWORD.getMessage());
        }

        String token = jwtTokenProvider.generateToken(member.getEmail(), member.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", member.getRole());

        return response;
    }

    @Transactional(readOnly = true)
    public void resignMember(String email, String password) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_USER.getMessage()));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UnauthorizedException(ErrorStatus.UNAUTHORIZED_PASSWORD.getMessage());
        }

        // Soft Delete
        // member.resign();

        // Hard Delete
        postRepository.deleteAllByMember(member);
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberInfoResponseDto getMyInfo(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_USER.getMessage()));


        return MemberInfoResponseDto.toDto(member);
    }

    @Transactional
    public void updateMemberInfo(String email, UpdateMemberRequestDto request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_USER.getMessage()));

        if (!member.getPhoneNumber().equals(request.getPhoneNumber())
                && memberRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_PHONE.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_DUPLICATE_PHONE.getMessage());
        }

        if (!member.getNickname().equals(request.getNickname())) {
            member.updateNickname(request.getNickname());
        }

        if (!member.getPhoneNumber().equals(request.getPhoneNumber())) {
            member.updatePhoneNumber(request.getPhoneNumber());
        }

        if (!member.getAddress().equals(request.getAddress())) {
            member.updateAddress(request.getAddress());
        }

        if (member.getAge() != request.getAge()) {
            member.updateAge(request.getAge());
        }
    }


}
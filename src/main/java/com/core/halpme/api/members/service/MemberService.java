package com.core.halpme.api.members.service;

import com.core.halpme.api.members.dto.LoginRequestDto;
import com.core.halpme.api.members.dto.RegisterRequestDto;
import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.jwt.JwtTokenProvider;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.common.exception.BaseException;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    @Transactional
    public void signup(RegisterRequestDto request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getMessage());
        }

        if (memberRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_NICKNAME.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_DUPLICATE_NICKNAME.getMessage());
        }

        if (memberRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BaseException(HttpStatus.BAD_REQUEST, ErrorStatus.BAD_REQUEST_DUPLICATE_PHONE.getMessage());
        }

        Address address = Address.builder()
                .city(request.getCity())
                .district(request.getDistrict())
                .dong(request.getDong())
                .build();

        Member member = Member.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .age(request.getAge())
                .note(request.getSpecialNote())
                .gender(request.getGender())
                .memberType(request.getMemberType())
                .role(request.getRole())
                .address(address)
                .build();

        memberRepository.save(member);
    }


    @Transactional(readOnly = true)
    public Map<String, Object> login(LoginRequestDto request) {
        // id = email, pw = password

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호를 다시 확인하세요."));

        if (!passwordEncoder.matches(request.getPw(), member.getPassword())) {
            throw new IllegalArgumentException("이메일 혹은 비밀번호를 다시 확인하세요.");
        }

        String token = jwtTokenProvider.generateToken(member.getEmail(),member.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", member.getRole());

        return response;
    }

}
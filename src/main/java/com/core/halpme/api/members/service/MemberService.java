package com.core.halpme.api.members.service;

import com.core.halpme.api.members.dto.LoginRequest;
import com.core.halpme.api.members.dto.RegisterRequest;
import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.entity.Role;
import com.core.halpme.api.members.jwt.JwtTokenProvider;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.common.exception.BaseException;
import com.core.halpme.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    public void signup(RegisterRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_DUPLICATE_EMAIL.getMessage());
        }

        if (memberRepository.findByNickname(request.getUsername()).isPresent()) {
            throw new BaseException(ErrorStatus.BAD_REQUEST_DUPLICATE_NICKNAME.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_DUPLICATE_NICKNAME.getMessage());
        }

        if (memberRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new BaseException(HttpStatus.BAD_REQUEST, ErrorStatus.BAD_REQUEST_DUPLICATE_PHONE.getMessage());
        }

        Address address = new Address();
        address.setAddressDetail(request.getAddressDetail());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDong(request.getDong());

        Member member = Member.builder()
                .nickname(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .age(request.getAge())
                .note(request.getSpecialNote())
                .gender(request.getGender())
                .memberType(request.getMemberType())
                .role(Role.MEMBER)
                .address(address)
                .build();

        memberRepository.save(member);
    }

    public String login(LoginRequest request) {
        // id = email, pw = password

        // 이메일 검증
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.BAD_REQUEST_INVALID_EMAIL.getHttpStatus(),
                        ErrorStatus.BAD_REQUEST_INVALID_EMAIL.getMessage()
                ));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPw(), member.getPassword())) {
            throw new BaseException(
                    ErrorStatus.BAD_REQUEST_INVALID_PASSWORD.getHttpStatus(),
                    ErrorStatus.BAD_REQUEST_INVALID_PASSWORD.getMessage()
            );
        }

        return jwtTokenProvider.generateToken(member.getEmail());
    }

}
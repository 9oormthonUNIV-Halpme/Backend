package com.core.halpme.domain.user.service;

import com.core.halpme.common.util.JwtTokenProvider;
import com.core.halpme.domain.Address;
import com.core.halpme.domain.user.dto.LoginRequest;
import com.core.halpme.domain.user.dto.RegisterRequest;
import com.core.halpme.domain.user.entity.User;
import com.core.halpme.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    public void signup(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        Address address = new Address();
        address.setAddressDetail(request.getAddressDetail());
        address.setLatitute(request.getLatitute());
        address.setLongitude(request.getLongitude());

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .address(address)  // Address 객체 주입
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        // id = email, pw = password

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        if (!passwordEncoder.matches(request.getPw(), user.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        return jwtTokenProvider.generateToken(user.getEmail()); // 또는 user.getId() 등
    }

}

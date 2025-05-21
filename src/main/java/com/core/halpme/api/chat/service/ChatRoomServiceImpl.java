package com.core.halpme.api.chat.service;

import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomRequestDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.repository.ChatRoomRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.jwt.SecurityUtil;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.common.exception.BaseException;
import com.core.halpme.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SecurityUtil securityUtil;
    private final MessageReadStatusRepository messageReadStatusRepository;

    @Override
    public CreateChatRoomResponseDto createChatRoomForPersonal(CreateChatRoomRequestDto chatRoomRequest) {
        String requesterEmail = securityUtil.getCurrentMemberUsername(); // 로그인 사용자

        if (!requesterEmail.equals(chatRoomRequest.getRoomMakerEmail())) {
            throw new BaseException(
                    ErrorStatus.UNAUTHORIZED_USER.getHttpStatus(),
                    "로그인한 사용자 정보가 요청자 정보와 일치하지 않습니다."
            );
        }

        Member roomMaker = memberRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.NOT_FOUND_USER.getHttpStatus(),
                        "방 생성자 정보를 찾을 수 없습니다."
                ));

        Member guest = memberRepository.findByEmail(chatRoomRequest.getGuestEmail())
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.NOT_FOUND_USER.getHttpStatus(),
                        "상대방(게스트) 정보를 찾을 수 없습니다."
                ));

        // 이미 존재하는 DM방이 있는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findRoomByTwoMembers(
                requesterEmail, chatRoomRequest.getGuestEmail()
        );

        if (existingRoom.isPresent()) {
            return new CreateChatRoomResponseDto(
                    roomMaker.getEmail(),
                    guest.getEmail(),
                    existingRoom.get().getId()
            );
        }

        // 새로 생성
        ChatRoom newRoom = ChatRoom.create();
        newRoom.addMembers(roomMaker, guest);
        chatRoomRepository.save(newRoom);

        return new CreateChatRoomResponseDto(
                roomMaker.getEmail(),
                guest.getEmail(),
                newRoom.getId()
        );
    }


    @Override
    public List<ChatRoomDto> getChatRoomsForUser(String email) {
        List<ChatRoom> rooms = chatRoomRepository.findAllByMemberEmail(email);
        return rooms.stream()
                .map(room -> ChatRoomDto.fromEntity(room, email, messageReadStatusRepository))
                .toList();
    }

}
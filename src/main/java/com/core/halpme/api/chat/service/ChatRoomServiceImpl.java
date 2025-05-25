package com.core.halpme.api.chat.service;

import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.repository.ChatMessageRepository;
import com.core.halpme.api.chat.repository.ChatRoomRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.jwt.SecurityUtil;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.common.exception.BaseException;
import com.core.halpme.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SecurityUtil securityUtil;
    private final MessageReadStatusRepository messageReadStatusRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public CreateChatRoomResponseDto createChatRoomForPersonal(String roomMakerEmail, String guestEmail) {

        Member roomMaker = memberRepository.findByEmail(roomMakerEmail)
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.NOT_FOUND_USER.getHttpStatus(),
                        "방 생성자 정보를 찾을 수 없습니다."
                ));

        Member guest = memberRepository.findByEmail(guestEmail)
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.NOT_FOUND_USER.getHttpStatus(),
                        "상대방(게스트) 정보를 찾을 수 없습니다."
                ));

        // 이미 존재하는 DM방이 있는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findRoomByTwoMembers(roomMakerEmail, guestEmail);

        if (existingRoom.isPresent()) {
            return new CreateChatRoomResponseDto(
                    roomMakerEmail,
                    guestEmail,
                    existingRoom.get().getId()
            );
        }

        // 새로 생성
        ChatRoom newRoom = ChatRoom.create(roomMaker);
        newRoom.addMembers(roomMaker, guest);
        chatRoomRepository.save(newRoom);

        return new CreateChatRoomResponseDto(
                roomMakerEmail,
                guestEmail,
                newRoom.getId()
        );
    }


    public List<ChatRoomDto> getChatRoomsForUser(String userEmail) {
        List<ChatRoom> rooms = chatRoomRepository.findAllByMemberEmail(userEmail);

        return rooms.stream()
                .map(room -> ChatRoomDto.fromEntity(room, userEmail, messageReadStatusRepository))
                .toList();
    }


}
package com.core.halpme.api.chat.service;

import com.core.halpme.api.chat.dto.ChatRoomDto;
import com.core.halpme.api.chat.dto.ChatRoomIdToPostIdDto;
import com.core.halpme.api.chat.dto.CreateChatRoomResponseDto;
import com.core.halpme.api.chat.dto.OpponentInfoDto;
import com.core.halpme.api.chat.entity.ChatRoom;
import com.core.halpme.api.chat.repository.ChatMessageRepository;
import com.core.halpme.api.chat.repository.ChatRoomRepository;
import com.core.halpme.api.chat.repository.MessageReadStatusRepository;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.jwt.SecurityUtil;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.repository.PostRepository;
import com.core.halpme.common.exception.BadRequestException;
import com.core.halpme.common.exception.BaseException;
import com.core.halpme.common.exception.NotFoundException;
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
    private final ChatMessageRepository chatMessageRepository;
    private final PostRepository postRepository;

    @Override
    public CreateChatRoomResponseDto createChatRoomForPersonal(String roomMakerEmail, Long guestPostId) {

        Member roomMaker = memberRepository.findByEmail(roomMakerEmail)
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.NOT_FOUND_USER.getHttpStatus(),
                        "방 생성자 정보를 찾을 수 없습니다."
                ));

        // PostId -> Post
        Post guestPost = postRepository.findById(guestPostId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_POST.getMessage()));
        if (guestPost.getMember() == null) {
            throw new NotFoundException(ErrorStatus.NOT_FOUND_USER.getMessage());
        }

        // Post -> Member.email
        String guestEmail = guestPost.getMember().getEmail();

        // 자기 자신과는 채팅 불가
        if (roomMakerEmail.equals(guestEmail)) {
            throw new BadRequestException(ErrorStatus.BAD_REQUEST_INVALID_REQUEST.getMessage());
        }

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
        ChatRoom newRoom = ChatRoom.create(roomMaker, guestPostId);
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


    public OpponentInfoDto getChatOpponentInfo(String roomId, String currentUserEmail) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.NOT_FOUND_CHATROOM.getHttpStatus(),
                        ErrorStatus.NOT_FOUND_CHATROOM.getMessage()
                ));


        String opponentNickname = room.getChatRoomMembers().stream()
                .filter(member -> !member.getEmail().equals(currentUserEmail))
                .map(Member::getNickname)
                .findFirst()
                .orElse("알 수 없음");


        String identity = room.getRoomMaker().getEmail().equals(currentUserEmail) ? "봉사참여" : "도움요청";

        return new OpponentInfoDto(opponentNickname, identity);
    }


    @Override
    public ChatRoomIdToPostIdDto getPostIdByChatRoomId(String roomId) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BaseException(
                        ErrorStatus.NOT_FOUND_CHATROOM.getHttpStatus(),
                        ErrorStatus.NOT_FOUND_CHATROOM.getMessage()
                ));

        Long postId = room.getGuestPostId();

        if (postId == null) {
            throw new BaseException(
                    ErrorStatus.NOT_FOUND_RELATED_POST.getHttpStatus(),
                    ErrorStatus.NOT_FOUND_RELATED_POST.getMessage()
            );
        }

        return new ChatRoomIdToPostIdDto(postId);
    }
}
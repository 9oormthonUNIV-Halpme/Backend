package com.core.halpme.api.post.service;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.api.post.dto.MyPostListResponseDto;
import com.core.halpme.api.post.dto.PostCreateRequestDto;
import com.core.halpme.api.post.dto.PostDetailResponseDto;
import com.core.halpme.api.post.dto.PostTotalListResponseDto;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.repository.PostRepository;

import com.core.halpme.common.exception.NotFoundException;
import com.core.halpme.common.exception.UnauthorizedException;
import com.core.halpme.common.response.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 봉사 신청글 생성
    @Transactional
    public void createPost(String email, PostCreateRequestDto request) throws NotFoundException, UnauthorizedException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_USER.getMessage()));

        Address address = request.getAddress();

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .requestDate(request.getRequestDate())
                .requestTime(request.getRequestTime())
                .address(address)
                .member(member)
                .build();

        postRepository.save(post);
    }

    // 내 봉사 신청글 전체 조회
    @Transactional(readOnly = true)
    public List<MyPostListResponseDto> getMyPostList(String email) {

        List<Post> myPosts = postRepository.findPostByMemberEmail(email);

        return myPosts.stream()
                .map(MyPostListResponseDto::toDto)
                .toList();
    }

    // 전체 봉사 신청글 조회
    public List<PostTotalListResponseDto> getTotalPostList() {

        List<Post> posts = postRepository.findAll();

        List<Post> sortedPosts = posts.stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .toList();

        return sortedPosts.stream()
                .map(PostTotalListResponseDto::toDto)
                .toList();
    }

    // 봉사 신청글 상세 조회
    public PostDetailResponseDto getPostDetail(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_RESOURCE.getMessage()));

        return PostDetailResponseDto.toDto(post);
    }

    // 봉사 신청글 수정
    @Transactional
    public PostDetailResponseDto updatePost(Long postId, String email, PostCreateRequestDto request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_RESOURCE.getMessage()));

        if(!post.getMember().getEmail().equals(email)) {
            throw new UnauthorizedException(ErrorStatus.BAD_REQUEST_POST_WRITER_NOT_SAME_USER.getMessage());
        }

        Address address = request.getAddress();

        post.updateTitle(request.getTitle());
        post.updateContent(request.getContent());
        post.updateAddress(address);

        return PostDetailResponseDto.toDto(post);
    }

    // 봉사 신청글 삭제
    @Transactional
    public void deletePost(Long postId, String email) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_RESOURCE.getMessage()));

        if(!post.getMember().getEmail().equals(email)) {
            throw new UnauthorizedException(ErrorStatus.BAD_REQUEST_POST_WRITER_NOT_SAME_USER.getMessage());
        }

        postRepository.delete(post);
    }
}

package com.core.halpme.api.post.service;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.api.post.dto.PostCreateRequest;
import com.core.halpme.api.post.dto.PostResponse;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.repository.PostRepository;

import com.core.halpme.common.exception.NotFoundException;
import com.core.halpme.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 게시물 생성시
    @Transactional
    public PostResponse createPost(String email, PostCreateRequest request) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 사람은 존재하지 않습니다."));

        Address address = Address.builder()
                .city(request.getCity())
                .district(request.getDistrict())
                .dong(request.getDong())
                .addressDetail(request.getAddressDetail())
                .build();

        Post post = Post.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .address(address)
                .build();

        postRepository.save(post);

        return new PostResponse(post);
    }

    //시, 구, 동 기준으로 게시물 조회
    @Transactional(readOnly = true)
    public List<PostResponse> findPostByAddress(String city, String district, String dong, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일로 회원을 찾을 수 없습니다."));
        List<Post> posts = postRepository.findByAddress_CityAndAddress_DistrictAndAddress_DongAndMember_Email(city, district, dong, email);

        // Entity -> DTO 변환해줘서 전환!
        return posts.stream()
                .map(PostResponse::new)

                //위에서 변환된 DTO를 다시 리스트화 시켜서 반환
                .collect(Collectors.toList());
    }


    //게시글 조회
    @Transactional(readOnly = true)
    public PostResponse getMyPost(String email, Long postId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다."));

        // 작성자 확인
        if (!post.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 조회할 수 있습니다.");
        }

        return new PostResponse(post);
    }


    //게시물 수정
    @Transactional
    public PostResponse updatePost(Long postId, String email, PostCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));

        // 본인 여부 확인(jwt)
        if(!post.getMember().getEmail().equals(email)) {
            throw new UnauthorizedException("게시글을 수정할 권한이 없습니다.");
        }

        //Address 객체를 먼저 빌더로 생성
        Address address = Address.builder()
                .city(request.getCity())
                .district(request.getDistrict())
                .dong(request.getDong())
                .addressDetail(request.getAddressDetail())
                .build();

        //수정 내용 반영
        post.update(request.getTitle(), request.getContent(), address);

        postRepository.save(post);

        return new PostResponse(post);
    }

    //게시물 삭제
    @Transactional
    public void deletePost(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("게시글을 찾을 수 없습니다."));
        if(!post.getMember().getEmail().equals(email)) {
            throw new UnauthorizedException("게시글을 삭제할 권한이 없습니다.");
        }


        postRepository.delete(post);
    }
}

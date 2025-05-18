package com.core.halpme.api.post.service;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.members.repository.MemberRepository;
import com.core.halpme.api.post.dto.PostCreateRequest;
import com.core.halpme.api.post.dto.PostResponse;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.repository.PostRepository;

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
    public PostResponse createPost(String username, PostCreateRequest request) {
        Member member = memberRepository.findByNickname(username)
                .orElseThrow(() -> new IllegalStateException("해당 사람은 존재하지 않습니다."));
        Post post = Post.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .address(new Address(
                        request.getCity(),
                        request.getDistrict(),
                        request.getDong()
                ))
                .build();
        postRepository.save(post);

        return new PostResponse(post);
    }

    //시, 구, 동 기준으로 게시물 조회
    @Transactional(readOnly = true)
    public List<PostResponse> findPostByAddress(String city, String district, String dong) {
        List<Post> posts = postRepository.findByCityAndDistrictAndDong(city, district, dong);

        //Entitiy -> DTO 변환해줘서 전환!
        return posts.stream()
                .map(PostResponse::new)

                //위에서 변환된 DTO를 다시 리스트화 시켜서 반환
                .collect(Collectors.toList());
    }
}

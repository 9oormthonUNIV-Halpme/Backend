package com.core.halpme.api.post.entity;

import com.core.halpme.api.members.entity.Address;
import com.core.halpme.api.members.entity.Member;
import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PostMemberType postMemberType;

    @Enumerated(EnumType.STRING)
    private ActivityStatus activityStatus;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // Post-Member 양방향 연관관계 값 설정 메서드
    public void setMember(Member member) {
        this.member = member;
        if (!member.getPosts().contains(this)) {
            member.getPosts().add(this);
        }
    }

    // Post 이미지 추가 메서드
    public void addImages(List<String> imageUrls) {
        for (String url : imageUrls) {
            PostImage postImage = PostImage.builder()
                    .imageUrl(url)
                    .post(this)
                    .build();
            this.images.add(postImage);
        }
    }

    // Post 이미지 삭제 메서드 (양방향 연관관계 해제)
    public void removeImage(PostImage image) {
        this.images.remove(image);
        image.setPost(null);
    }

    // 이미지 리스트 조회 시 불변 객체 리스트 반환
    public List<PostImage> getImages() {
        return Collections.unmodifiableList(this.images);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Post post = (Post) obj;
        return id != null && id.equals(post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

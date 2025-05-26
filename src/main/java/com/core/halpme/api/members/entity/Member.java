package com.core.halpme.api.members.entity;

import com.core.halpme.api.post.entity.Post;
import com.core.halpme.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Table(name = "members")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "nickname", nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "age")
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    // 멤버 권한 설정 메서드
    public Member authorizeMember() {
        return this.toBuilder()
                .role(Role.ROLE_MEMBER)
                .build();
    }

    // 멤버 전화번호 변경 메서드
    public void updatePhoneNumber(String updatedPhoneNumber) {
        this.phoneNumber = updatedPhoneNumber;
    }

    // 멤버 주소 변경 메서드
    public void updateAddress(Address updatedAddress) {
        this.address = updatedAddress;
    }

    // 닉네임 변경 메서드
    public void updateNickname(String nickname){this.nickname=nickname;}

    public void updateAge(int age){this.age=age;}
    // posts 필드에 post 추가 메서드
    public void addPost(Post post) {
        this.posts.add(post);
        post.setMember(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return id != null && id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

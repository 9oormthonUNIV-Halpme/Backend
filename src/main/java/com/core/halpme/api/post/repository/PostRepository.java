package com.core.halpme.api.post.repository;

import com.core.halpme.api.post.entity.Post;

import java.util.List;

//시, 구, 동이 같은 게시물 조회할때 사용
//address.city → address_City
//member.email → member_Email 이런식으로 JPA가 필드를 찾아감
public interface PostRepository extends org.springframework.data.jpa.repository.JpaRepository<Post, Long> {
    List<Post> findByAddress_CityAndAddress_DistrictAndAddress_DongAndMember_Email(String city, String district, String dong, String email);
}

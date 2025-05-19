package com.core.halpme.api.post.repository;

import com.core.halpme.api.post.entity.Post;

import java.util.List;


// Post의 Address 기준으로 검색
public interface PostRepository extends org.springframework.data.jpa.repository.JpaRepository<Post, Long> {
    List<Post> findByAddress_CityAndAddress_DistrictAndAddress_DongAndMember_Email(String city, String district, String dong, String email);
}

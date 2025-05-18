package com.core.halpme.api.post.repository;

import com.core.halpme.api.post.entity.Post;

import java.util.List;

//시, 구, 동이 같은 게시물 조회할때 사용
public interface PostRepository extends org.springframework.data.jpa.repository.JpaRepository<Post, Long> {
    List<Post> findByCityAndDistrictAndDong(String city, String district, String dong);
}

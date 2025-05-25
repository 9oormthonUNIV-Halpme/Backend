package com.core.halpme.api.post.repository;

import com.core.halpme.api.members.entity.Member;
import com.core.halpme.api.post.entity.Post;
import com.core.halpme.api.post.entity.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostByMemberEmail(String memberEmail);

    void deleteAllByMember(Member member);

    List<Post> findByVolunteerEmailAndPostStatusOrderByRequestDateDesc(String email, PostStatus postStatus);
}

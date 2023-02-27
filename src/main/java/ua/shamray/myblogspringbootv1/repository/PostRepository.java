package ua.shamray.myblogspringbootv1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.shamray.myblogspringbootv1.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.account.id = :id")
    List<Post> findPostsByAccountID(@Param("id") Long id);
}

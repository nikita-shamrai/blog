package ua.shamray.myblogspringbootv1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.shamray.myblogspringbootv1.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}

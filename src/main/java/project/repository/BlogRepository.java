package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long>{
}

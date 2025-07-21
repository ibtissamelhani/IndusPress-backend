package org.ibtissam.induspress.repository;

import org.ibtissam.induspress.model.Article;
import org.ibtissam.induspress.model.Category;
import org.ibtissam.induspress.model.Status;
import org.ibtissam.induspress.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> , SearchArticleRepository{
    Page<Article> findByAuthor(User author, Pageable pageable);
    Page<Article> findByStatusOrderByCreatedAtDesc(Status status, Pageable pageable);

}

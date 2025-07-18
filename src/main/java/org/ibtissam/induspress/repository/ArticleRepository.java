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

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    List<Article> findByAuthor(User author);
    List<Article> findByStatus(Status status);
    List<Article> findByCategory(Category category);
    Page<Article> findByStatusOrderByCreatedAtDesc(Status status, Pageable pageable);
    List<Article> findByAuthorAndStatus(User author, Status status);

    @Query("SELECT a FROM Article a WHERE " +
            "(:categoryId IS NULL OR a.category.id = :categoryId) AND " +
            "(:authorFirstName IS NULL OR LOWER(a.author.firstName) LIKE LOWER(CONCAT('%', :authorFirstName, '%'))) AND " +
            "(:authorLastName IS NULL OR LOWER(a.author.lastName) LIKE LOWER(CONCAT('%', :authorLastName, '%'))) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:startDate IS NULL OR a.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR a.createdAt <= :endDate)")
    Page<Article> findArticlesWithFilters(
            @Param("categoryId") UUID categoryId,
            @Param("authorFirstName") String authorFirstName,
            @Param("authorLastName") String authorLastName,
            @Param("status") Status status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}

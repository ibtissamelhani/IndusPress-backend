package org.ibtissam.induspress.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.ibtissam.induspress.dto.article.ArticleFilterRequest;
import org.ibtissam.induspress.dto.article.ArticleMapper;
import org.ibtissam.induspress.dto.article.ArticleResponse;
import org.ibtissam.induspress.model.Article;
import org.ibtissam.induspress.model.Status;
import org.ibtissam.induspress.repository.SearchArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class SearchArticleRepositoryImpl implements SearchArticleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final ArticleMapper mapper;


    @Override
    public Page<ArticleResponse> findByFilters(ArticleFilterRequest filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Article> query = cb.createQuery(Article.class);
        Root<Article> article = query.from(Article.class);
        List<Predicate> predicates = new ArrayList<>();

        Join<?, ?> authorJoin = article.join("author", JoinType.LEFT);
        Join<?, ?> categoryJoin = article.join("category", JoinType.LEFT);

        if (filter.getCategoryId() != null) {
            predicates.add(cb.equal(categoryJoin.get("id"), filter.getCategoryId()));
        }

        if (filter.getAuthorName() != null && !filter.getAuthorName().isBlank()) {
            String pattern = "%" + filter.getAuthorName().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(authorJoin.get("firstName")), pattern),
                    cb.like(cb.lower(authorJoin.get("lastName")), pattern)
            ));
        }

        if (filter.getFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(article.get("createdAt"), filter.getFrom().atStartOfDay()));
        }
        if (filter.getTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(article.get("createdAt"), filter.getTo().atTime(23, 59, 59)));
        }
        predicates.add(cb.equal(article.get("status"), Status.VALIDE));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(article.get("createdAt")));

        List<Article> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Article> countRoot = countQuery.from(Article.class);
        Join<?, ?> countAuthorJoin = countRoot.join("author", JoinType.LEFT);
        Join<?, ?> countCategoryJoin = countRoot.join("category", JoinType.LEFT);
        List<Predicate> countPredicates = new ArrayList<>();

        if (filter.getCategoryId() != null) {
            countPredicates.add(cb.equal(countCategoryJoin.get("id"), filter.getCategoryId()));
        }
        if (filter.getAuthorName() != null && !filter.getAuthorName().isBlank()) {
            String pattern = "%" + filter.getAuthorName().toLowerCase() + "%";
            countPredicates.add(cb.or(
                    cb.like(cb.lower(countAuthorJoin.get("firstName")), pattern),
                    cb.like(cb.lower(countAuthorJoin.get("lastName")), pattern)
            ));
        }
        if (filter.getFrom() != null) {
            countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("createdAt"), filter.getFrom().atStartOfDay()));
        }
        if (filter.getTo() != null) {
            countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("createdAt"), filter.getTo().atTime(23, 59, 59)));
        }

        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        List<ArticleResponse> responseList = resultList.stream().map(mapper::toDto).toList();
        return new PageImpl<>(responseList, pageable, total);
    }
}

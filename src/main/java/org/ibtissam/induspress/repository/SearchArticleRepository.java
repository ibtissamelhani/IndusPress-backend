package org.ibtissam.induspress.repository;

import org.ibtissam.induspress.dto.article.ArticleFilterRequest;
import org.ibtissam.induspress.dto.article.ArticleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchArticleRepository {
    Page<ArticleResponse> findByFilters(ArticleFilterRequest filter, Pageable pageable);
}

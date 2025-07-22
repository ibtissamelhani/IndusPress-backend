package org.ibtissam.induspress.web.rest;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ibtissam.induspress.dto.article.ArticleFilterRequest;
import org.ibtissam.induspress.dto.article.ArticleRequest;
import org.ibtissam.induspress.dto.article.ArticleResponse;
import org.ibtissam.induspress.model.Article;
import org.ibtissam.induspress.model.Status;
import org.ibtissam.induspress.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/articles")
@AllArgsConstructor
public class ArticleController {

    private ArticleService articleService;


    @PostMapping
    public ResponseEntity<ArticleResponse> create(@Valid @RequestBody ArticleRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(articleService.getById(id));
    }

    @GetMapping("/All")
    public ResponseEntity<Page<ArticleResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(articleService.getAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> getValidatedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(articleService.getValidatedPublicArticles(PageRequest.of(page, size)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        articleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Article supprimée avec succès");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody ArticleRequest dto) {
        return ResponseEntity.ok(articleService.update(id, dto));
    }

    @PostMapping("/filter")
    public Page<ArticleResponse> filterArticles(
            @RequestBody ArticleFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return articleService.filterArticles(filter, PageRequest.of(page, size));
    }

    @GetMapping("/my-articles")
    public ResponseEntity<Page<ArticleResponse>> getMyArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ArticleResponse> articles = articleService.getArticlesByUser(pageable);
        return ResponseEntity.ok(articles);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<ArticleResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam Status status) {

        ArticleResponse updatedArticle = articleService.updateStatus(id, status);
        return ResponseEntity.ok(updatedArticle);
    }

}

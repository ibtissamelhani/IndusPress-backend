package org.ibtissam.induspress.web.rest;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ibtissam.induspress.dto.article.ArticleRequest;
import org.ibtissam.induspress.dto.article.ArticleResponse;
import org.ibtissam.induspress.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(articleService.getAll(PageRequest.of(page, size)));
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
}

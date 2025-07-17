package org.ibtissam.induspress.service;

import lombok.RequiredArgsConstructor;
import org.ibtissam.induspress.dto.article.ArticleMapper;
import org.ibtissam.induspress.dto.article.ArticleRequest;
import org.ibtissam.induspress.dto.article.ArticleResponse;
import org.ibtissam.induspress.exception.ArticleNotFoundException;
import org.ibtissam.induspress.exception.CategoryNotFoundException;
import org.ibtissam.induspress.exception.UserNotFoundException;
import org.ibtissam.induspress.model.Article;
import org.ibtissam.induspress.model.Category;
import org.ibtissam.induspress.model.Status;
import org.ibtissam.induspress.model.User;
import org.ibtissam.induspress.repository.ArticleRepository;
import org.ibtissam.induspress.repository.CategoryRepository;
import org.ibtissam.induspress.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ArticleMapper mapper;


    public ArticleResponse create(ArticleRequest dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Catégorie non trouvée"));
        User currentUser = getCurrentUser();

        Article article = mapper.toEntity(dto);
        article.setAuthor(currentUser);
        article.setCategory(category);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        return mapper.toDto(articleRepository.save(article));
    }


    public ArticleResponse update(UUID id, ArticleRequest dto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé"));
        mapper.updateEntityFromDto(dto, article);
        article.setUpdatedAt(LocalDateTime.now());
        return mapper.toDto(articleRepository.save(article));
    }


    public void delete(UUID id) {
        articleRepository.deleteById(id);
    }


    public ArticleResponse getById(UUID id) {
        return mapper.toDto(articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article non trouvé")));
    }


    public Page<ArticleResponse> getAll(Pageable peageable) {
        return articleRepository.findAll(peageable)
                .map(mapper::toDto);
    }


//    public Page<ArticleResponse> getValidatedPublicArticles() {
//        return articleRepository.findByStatusOrderByCreatedAtDesc(Status.VALIDE)
//                .map(mapper::toDto);
//    }



    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
    }
}

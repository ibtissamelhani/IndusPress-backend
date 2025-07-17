package org.ibtissam.induspress.dto.article;

import org.ibtissam.induspress.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    Article toEntity(ArticleRequest dto);

    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "author", source = "author.firstName")
    ArticleResponse toDto(Article entity);

    void updateEntityFromDto(ArticleRequest dto, @MappingTarget Article entity);
}

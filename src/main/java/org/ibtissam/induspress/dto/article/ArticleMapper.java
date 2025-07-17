package org.ibtissam.induspress.dto.article;

import org.ibtissam.induspress.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    Article toEntity(ArticleRequest dto);

    @Mapping(target = "category", source = "category.name")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.lastName", target = "authorLastName")
    ArticleResponse toDto(Article entity);

    void updateEntityFromDto(ArticleRequest dto, @MappingTarget Article entity);
}

package org.ibtissam.induspress.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.ibtissam.induspress.model.Status;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;

    @NotNull(message = "La catégorie est obligatoire")
    private UUID categoryId;

    private String coverImage;

    @NotNull(message = "Le statut est obligatoire")
    private Status status;
}

package org.ibtissam.induspress.dto.article;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponse {

    private UUID id;
    private String title;
    private String content;
    private String coverImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String category;
    private String status;
    private String authorFirstName;
    private String authorLastName;
}

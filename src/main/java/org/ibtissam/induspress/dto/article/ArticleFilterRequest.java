package org.ibtissam.induspress.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.ibtissam.induspress.model.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleFilterRequest {
    private UUID categoryId;
    private String authorName;
    private LocalDate from;
    private LocalDate to;
}

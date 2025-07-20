package org.ibtissam.induspress.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.ibtissam.induspress.model.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleFilterDTO {
    private UUID categoryId;
    private String authorFirstName;
    private String authorLastName;
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    // Paramètres de pagination
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";


}

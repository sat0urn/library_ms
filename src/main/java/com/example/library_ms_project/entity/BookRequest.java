package com.example.library_ms_project.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "BookRequest")
public class BookRequest {
    private String userId;
    private String bookId;
    private int returnDays;
}

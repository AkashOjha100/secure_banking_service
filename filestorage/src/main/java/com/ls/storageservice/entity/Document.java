package com.ls.storageservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String originalFileName;

    private String storedFileName;

    private String contentType;

    private Long size;

    private String bucketName;

    private String objectKey;

    private String status;

    private Long uploadedBy;

    private LocalDateTime uploadedAt;
}

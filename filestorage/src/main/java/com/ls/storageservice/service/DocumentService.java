package com.ls.storageservice.service;

import com.ls.storageservice.dto.FileResponse;
import com.ls.storageservice.entity.Document;
import com.ls.storageservice.repository.DocumentRepository;
import com.ls.storageservice.util.FileValidator;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class DocumentService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private DocumentRepository documentRepo;

    @Autowired
    private FileValidator validator;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public FileResponse upload(MultipartFile file, Long userId) throws Exception {
        validator.validate(file);

        String fileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(file.getInputStream(),
                        file.getSize(),
                        -1)
                .contentType(file.getContentType())
                .build()
        );

        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(1 , TimeUnit.HOURS)
                        .build()
        );

        Document document = new Document();

        document.setOriginalFileName(file.getOriginalFilename());
        document.setStoredFileName(fileName);
        document.setContentType(file.getContentType());
        document.setSize(file.getSize());
        document.setBucketName(bucketName);
        document.setObjectKey(fileName);
        document.setUploadedAt(LocalDateTime.now());
        document.setUploadedBy(userId);
        document.setStatus("Uploaded");
        documentRepo.save(document);

        return FileResponse.builder()
                .id(document.getId())
                .fileName(document.getOriginalFileName())
                .fileUrl(url)
                .contentType(document.getContentType())
                .size(document.getSize())
                .status(document.getStatus())
                .build();
    }

    public String getFileUrl(Long documentId)
            throws Exception {

        Document document = documentRepo.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("Document not found")
                );

        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(document.getBucketName())
                        .object(document.getObjectKey())
                        .method(Method.GET)
                        .expiry(1, TimeUnit.HOURS)
                        .build()
        );
    }

    public void delete(Long documentId)
            throws Exception {

        Document document = documentRepo.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("Document not found")
                );

        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(document.getBucketName())
                        .object(document.getObjectKey())
                        .build()
        );

        documentRepo.delete(document);
    }
}

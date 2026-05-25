package com.ls.storageservice.controller;

import com.ls.storageservice.dto.ApiResponse;
import com.ls.storageservice.dto.FileResponse;
import com.ls.storageservice.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    @Autowired
    private DocumentService docService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> upload(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ) throws Exception {

        //Long userId = 101L;
        Long userId = Long.parseLong(jwt.getSubject());

        return ResponseEntity.ok(
                docService.upload(file ,userId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getFile(
            @PathVariable Long id
    ) throws Exception {

        return ResponseEntity.ok(
                docService.getFileUrl(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable Long id
    ) throws Exception {

        docService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse(true, "File deleted")
        );
    }
}

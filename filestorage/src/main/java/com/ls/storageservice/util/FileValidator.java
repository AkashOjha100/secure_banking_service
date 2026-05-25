package com.ls.storageservice.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileValidator {

    public void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("The file has no content!");
        }
        if (file.getSize() > 5 * 1024 *1024) { //means 5 file size greater than 5 MB
            throw new IllegalArgumentException("The file size is too large!");

        }

        List<String> allowedTypes = List.of(
                "application/pdf",
                "image/png",
                "image/jpeg"
        );
        if(!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("The file type is not allowed!");
        }
    }
}

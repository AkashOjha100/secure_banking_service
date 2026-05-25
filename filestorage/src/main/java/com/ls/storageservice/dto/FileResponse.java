package com.ls.storageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FileResponse {

    private Long id;

    private String fileName;

    private String fileUrl;

    private String contentType;

    private Long size;

    private String status;
}

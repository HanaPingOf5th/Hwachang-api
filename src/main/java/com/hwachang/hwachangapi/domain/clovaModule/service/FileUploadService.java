package com.hwachang.hwachangapi.domain.clovaModule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final NcloudStorageService ncloudStorageService;

    public String uploadFile(MultipartFile file) throws IOException {
        return ncloudStorageService.uploadFile(file);
    }
}

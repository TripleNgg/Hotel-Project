package org.tripleng.likesidehotel.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file);
    void deleteFileByUrl(String fileName);
}

package com.projeto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${storage.upload-dir:uploads}")
    private String uploadDir;

    @Value("${storage.base-url:http://localhost:8081/uploads}")
    private String baseUrl;

    public String store(MultipartFile file) {
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String extension = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                extension = original.substring(original.lastIndexOf('.'));
            }

            String filename = UUID.randomUUID() + extension;
            Files.copy(file.getInputStream(), dir.resolve(filename));

            return baseUrl + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
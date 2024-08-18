package com.example.blog.service.impl;

import com.example.blog.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // File name
        String name = file.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();
        String fileName = "";
        if (name != null) {
            fileName = randomId.concat(name.substring(name.lastIndexOf(".")));
        }

        // FullPath
        String filePath = path + File.separator + fileName;

        // create folder
        File f = new File(path);
        if (!f.exists()) {
            boolean mkdir = f.mkdir();
        }

        // file copy
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String filePath = path + File.separator + fileName;
        return new FileInputStream(filePath);
    }
}

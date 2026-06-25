package com.ecommerce.sb_ecomm.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // FIle name of the current/orignal file
        String orignalFileName = file.getOriginalFilename();

        // Rename mat.jpg , randoid -> 1234 ->1234.jpg
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(orignalFileName.substring(orignalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        // check if path exists and create
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}

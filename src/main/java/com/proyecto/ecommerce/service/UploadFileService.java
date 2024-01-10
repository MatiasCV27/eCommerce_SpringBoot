package com.proyecto.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFileService {

    private String folder = "//opt//images//";
    private final Path root = Paths.get("images");

    public UploadFileService() {
        if (!Files.exists(root)) {
            try {
                Files.createDirectory(root);
            } catch (IOException e) {
                System.out.println("No se pudo crear el directorio");
                throw new RuntimeException(e);
            }
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            byte [] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } else {
            File fileSources = new File("//opt//default.jpg");
            File fileDest = new File("//default.jpg");
            InputStream in = new FileInputStream(fileSources);
            Path path = Paths.get(folder + fileDest);
            Files.write(path, in.readAllBytes());
        }
        return "default.jpg";
    }

    public void deleteImage(String nombre) {
        String ruta = "//opt//images//";
        File file = new File(ruta + nombre);
        file.delete();
    }
}

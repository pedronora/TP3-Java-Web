package br.edu.infnet.tp3javaweb.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadImage(MultipartFile img) {
        String imgName = UUID.randomUUID().toString() + "_" + img.getOriginalFilename();
        File imgFile = convertMultiPartFileToFile(img);
        s3Client.putObject(new PutObjectRequest(bucketName, imgName, imgFile));

        imgFile.delete();
        return s3Client.getUrl(bucketName, imgName).toString();
    }

    public void deleteImg(String imgName) {
        s3Client.deleteObject(bucketName, imgName);
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.out.println("Error converting multipartFile to file: " + e);
        }
        return convertedFile;
    }
}

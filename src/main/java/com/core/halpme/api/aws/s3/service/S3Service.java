package com.core.halpme.api.aws.s3.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.domain}")
    private String domain;

    public List<String> uploadChatMessageImages(String memberIdentifier, List<MultipartFile> files) throws IOException {

        String dir = "chat-images";
        List<String> imageUrls = new ArrayList<>();

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z')
                .withinRange('A', 'Z')
                .get();

        String randomString = generator.generate(16);

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String currentDateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String originalFilename = file.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                originalFilename = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            }

            // 파일명: {원파일이름}_{currentDateTime}.{확장자}
            String fileName = originalFilename + "_" + currentDateTime + extension;

            // 파일경로: chat-images/{memberEmailPrefix}/{랜덤문자(16자리)/파일명
            String fileKey = dir + "/" + memberIdentifier + "/" + randomString + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            imageUrls.add(domain + "/" + fileKey);
        }

        return imageUrls;
    }

    public void deleteFile(String imageUrl) {
        if (imageUrl != null && imageUrl.startsWith(domain)) {

            String fileKey = imageUrl.replace(domain + "/" + bucketName + "/", "");

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        }
    }
}

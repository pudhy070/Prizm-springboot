package com.example.aigatewayservice.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    public String uploadFileForAnalysis(MultipartFile file) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        return objectName;
    }

    /**
     * 파일을 지정된 버킷에 업로드하고 파일 URL을 반환합니다.
     * 버킷이 없으면 생성합니다.
     * @param file 업로드할 파일
     * @param bucketName 대상 버킷 이름
     * @return 업로드된 파일의 URL
     * @throws IOException 파일 처리 중 오류 발생 시
     */
    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            String objectName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            uploadFileForAnalysis(file); // 내부적으로 putObject를 호출합니다.
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(objectName).expiry(7, TimeUnit.DAYS).build());
        } catch (Exception e) {
            throw new IOException("Error uploading file to Minio", e);
        }
    }

    public String getPresignedUploadUrl(String objectName, int duration) throws ServerException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(duration, TimeUnit.MINUTES)
                        .build());
    }

    public String getPresignedDownloadUrl(String objectName, int duration) throws ServerException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(duration, TimeUnit.MINUTES)
                        .build());
    }
}
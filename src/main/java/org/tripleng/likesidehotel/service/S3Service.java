package org.tripleng.likesidehotel.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.tripleng.likesidehotel.exception.UploadFileException;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class S3Service implements FileService{
    private static final String BUCKET_NAME = "lakeside-hotel";
    private final AmazonS3 amazonS3;

    @Override
    public String uploadFile(MultipartFile file) {
        var filenameExtensions = StringUtils.getFilenameExtension(file.getOriginalFilename());
        var key = UUID.randomUUID() + "." + filenameExtensions;
        var metaData = new ObjectMetadata();
        metaData.setContentLength(file.getSize());
        metaData.setContentType(file.getContentType());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, file.getInputStream(), metaData);
            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);

            URL preSignedUrl = generatePresignedUrl(BUCKET_NAME, key);
            return preSignedUrl.toString();
        } catch (AmazonS3Exception e) {
            log.error("Amazon S3 Error: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while interacting with Amazon S3");
        } catch (IOException e) {
            log.error("IO Exception: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An exception occurred while processing the file");
        } catch (Exception e) {
            log.error("Unexpected Exception: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }


    public URL generatePresignedUrl(String bucketName, String key) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60; // 1 hour
        expiration.setTime(expTimeMillis);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    @Override
    public void deleteFileByUrl(String fileUrl) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(fileUrl).build();

        String bucketName = uriComponents.getPathSegments().get(0); // Lấy phần đầu tiên của path, tức là tên bucket
        String key = uriComponents.getPathSegments().get(1); // Lấy phần thứ hai của path, tức là key
        try {
            // Xóa tệp
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (Exception e) {
            // Xử lý ngoại lệ xóa tệp
            System.out.println(e.getMessage());
            // Ném ngoại lệ hoặc xử lý theo ý của bạn
        }
    }
}

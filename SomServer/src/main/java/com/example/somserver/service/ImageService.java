package com.example.somserver.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.somserver.config.S3Config;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {

    //S3Config 주입받기
    private S3Config s3Config;

    @Autowired
    public ImageService(S3Config s3Config) {

        this.s3Config = s3Config;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //서버환경에 따라 변경 필요 ex)리눅스: /home/ubuntu/~~~~~~~~~~~
    private String localLocation = "여기에 경로 입력";

    //이미지 s3에 업로드 메서드
    @Transactional
    public String uploadImage(MultipartFile file) throws IOException, AmazonServiceException, AmazonClientException {

        //이미지 파일에서 이름 및 확장자 추출
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        //이미지 파일 이름 유일성을 위해 uuid 생성
        String uuidFileName = UUID.randomUUID() + ext;

        //서버 환경에 저장할 경로 생성
        String localPath = localLocation + uuidFileName;

        //서버 환경에 이미지 파일 저장
        File localFile = new File(localPath);
        file.transferTo(localFile);

        //s3에 이미지 올리기
        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        //s3에 올린 이미지의 src 주소 가져오기
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        //서버 환경에 저장한 이미지 삭제
        localFile.delete();

        return s3Url;
    }

    //해당 이미지 s3에서 삭제 메서드
    @Transactional
    public void deleteImage(String imageUrl) throws AmazonServiceException, AmazonClientException {

        // 이미지 URL 에서 파일명 추출
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        // S3에서 이미지 삭제
        s3Config.amazonS3Client().deleteObject(bucket, fileName);
    }
}

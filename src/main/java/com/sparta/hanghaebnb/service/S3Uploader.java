package com.sparta.hanghaebnb.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile) throws IOException {
        //MultipartFile을 전달 받고
        //S3에 Multipartfile 타입은 전송이 안됩니다.
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        return upload(uploadFile);
    }

    private String upload(File uploadFile) {
        String fileName = "hanghaebnb" + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    //    전환된 File을 S3에 public 읽기 권한으로 put
//    외부에서 정적 파일을 읽을 수 있도록 하기 위함
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //    로컬에 생성된 File 삭제
//    Multipartfile -> File로 전환되면서 로컬에 파일 생성된것을 삭제합니다.
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        //getOriginalFilename으로 파일의 경로와 이름을 불러온다. -> 널값인지 체크후 파일 객체 생성 널이면 NullPointException
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        //createNewFile로 convertFile의 경로가 실제로 존재하는지 유무 확인 없다면 IOException
        if(convertFile.createNewFile()) {
            //파일 객체 생성
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());//fos 내부의 바이트 배열 데이터를 가져와서 저장
            }
            return Optional.of(convertFile); //Null인지 아닌지 판별
        }
        return Optional.empty(); //빈 객체를 반환
    }
}
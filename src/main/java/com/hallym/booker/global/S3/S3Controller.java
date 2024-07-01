package com.hallym.booker.global.S3;

import com.hallym.booker.global.S3.dto.S3ResponseUploadEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3service;

    @PostMapping("/S3/upload")
    public S3ResponseUploadEntity upload(@RequestParam("file")MultipartFile file, String dir) throws Exception {
        if(file.isEmpty()) {
            return new S3ResponseUploadEntity("file is empty", null);
        }

        return s3service.upload(file, dir);
    }
}

package com.outofcity.server.controller;

import com.outofcity.server.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {
    private final S3Service s3Service;

    @PostMapping("/asdpofjq1l1kjw1kfp1kdjf")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(s3Service.uploadToS3(multipartFile, token));
    }
}

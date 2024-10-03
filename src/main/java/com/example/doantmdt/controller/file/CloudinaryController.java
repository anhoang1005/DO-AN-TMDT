package com.example.doantmdt.controller.file;

import com.example.doantmdt.payload.ResponseBody;
import com.example.doantmdt.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CloudinaryController {

    @Autowired
    private FileService fileService;

    @PostMapping("/api/guest/file/upload")
    public ResponseEntity<ResponseBody> uploadOneFileApi(
            @RequestParam(name = "file")MultipartFile file){
        return new ResponseEntity<>(fileService.uploadToCloudinary(file), HttpStatus.OK);
    }

}

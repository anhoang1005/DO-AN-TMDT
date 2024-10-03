package com.example.doantmdt.service;

import com.example.doantmdt.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResponseBody uploadToCloudinary(MultipartFile file);
}

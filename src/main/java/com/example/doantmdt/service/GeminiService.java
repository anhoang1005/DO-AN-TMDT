package com.example.doantmdt.service;

import com.example.doantmdt.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface GeminiService {

    ResponseBody chatWithGeminiText(String message);

    ResponseBody chatWithGeminiImage(String message, MultipartFile file);
}

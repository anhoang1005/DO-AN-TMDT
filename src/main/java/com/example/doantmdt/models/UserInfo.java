package com.example.doantmdt.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
public class UserInfo {
    private MultipartFile image;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
}

package com.example.doantmdt.models.users;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UserRegisterInfo {
    private String firstName;
    private String lastName;
    private String dob;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
}

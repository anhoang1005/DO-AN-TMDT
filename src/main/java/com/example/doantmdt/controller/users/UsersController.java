package com.example.doantmdt.controller.users;

import com.example.doantmdt.models.users.UserRegisterInfo;
import com.example.doantmdt.payload.ResponseBody;
import com.example.doantmdt.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/api/all/users/detail")
    public ResponseEntity<ResponseBody> userGetUserDetailApi(
            @RequestParam("email") String email
    ){
        return ResponseEntity.ok(usersService.usersGetUsersDetail(email));
    }

    @PutMapping("/api/all/users/change-password")
    public ResponseEntity<ResponseBody> userChangePasswordApi(
            @RequestParam("email") String email,
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword
    ){
        return ResponseEntity.ok(usersService.usersChangePassword(email, oldPassword, newPassword));
    }

    @PutMapping("/api/all/users/change-info")
    public ResponseEntity<ResponseBody> userChangeInfoApi(
            @RequestBody UserRegisterInfo userInfo){
        return ResponseEntity.ok(usersService.usersChangeUserInfo(userInfo));
    }

    @PutMapping("/api/all/users/change-avatar")
    public ResponseEntity<ResponseBody> userChangeImageUrlApi(
            @RequestParam("email") String email,
            @RequestParam("file")MultipartFile file){
        return ResponseEntity.ok(usersService.usersChangeAvatar(email, file));
    }
}

package com.example.doantmdt.service;

import com.example.doantmdt.models.users.UserRegisterInfo;
import com.example.doantmdt.payload.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

public interface UsersService {

    ResponseBody usersChangePassword(String email, String oldPassword, String newPassword);

    ResponseBody usersChangeUserInfo(UserRegisterInfo info);

    ResponseBody usersChangeAvatar(String email, MultipartFile file);

    ResponseBody usersGetUsersDetail(String email);

}

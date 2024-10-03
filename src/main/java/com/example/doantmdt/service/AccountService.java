package com.example.doantmdt.service;

import com.example.doantmdt.models.users.UserRegisterInfo;
import com.example.doantmdt.payload.JwtData;
import com.example.doantmdt.payload.ResponseBody;

public interface AccountService {

    JwtData loginUsers(String username, String password);

    ResponseBody registerUsers(UserRegisterInfo user);

    ResponseBody checkVerifyCodeRegister(String email, String verifyCode);

    ResponseBody userForgotPassword(String email);

    ResponseBody checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode);
}

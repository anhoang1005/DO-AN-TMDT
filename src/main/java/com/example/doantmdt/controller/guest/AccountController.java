package com.example.doantmdt.controller.guest;

import com.example.doantmdt.models.users.UserRegisterInfo;
import com.example.doantmdt.payload.JwtData;
import com.example.doantmdt.payload.ResponseBody;
import com.example.doantmdt.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/api/guest/users/login")
    public ResponseEntity<ResponseBody> loginUsersApi(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        ResponseBody responseBody = new ResponseBody(
                accountService.loginUsers(username, password),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/api/guest/users/register")
    public ResponseEntity<ResponseBody> registerUserApi(
            @RequestBody UserRegisterInfo user) {
        return new ResponseEntity<>(accountService.registerUsers(user), HttpStatus.OK);
    }

    @GetMapping("/api/guest/users/check-register")
    public ResponseEntity<ResponseBody> checkRegisterApi(
            @RequestParam("email") String email,
            @RequestParam("verify_code") String verifyCode
    ) {
        return new ResponseEntity<>(accountService.checkVerifyCodeRegister(email, verifyCode), HttpStatus.OK);
    }

    @GetMapping("/api/guest/users/forgot-password")
    public ResponseEntity<ResponseBody> forgotPasswordApi(
            @RequestParam("email") String email
    ) {
        return new ResponseEntity<>(accountService.userForgotPassword(email), HttpStatus.OK);
    }

    @PutMapping("/api/guest/users/check-forgot")
    public ResponseEntity<ResponseBody> checkVerifyForgotPasswordApi(
            @RequestParam("email") String email,
            @RequestParam("new_password") String newPassword,
            @RequestParam("verify_code") String verifyCode
    ) {
        return new ResponseEntity<>(accountService.checkVerifyCodeForgotPassword(email, newPassword, verifyCode), HttpStatus.OK);
    }
}

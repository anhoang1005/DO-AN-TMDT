package com.example.doantmdt.service.implement;

import com.example.doantmdt.entities.RoleEntity;
import com.example.doantmdt.entities.UsersEntity;
import com.example.doantmdt.exceptions.ghn.RequestNotFoundException;
import com.example.doantmdt.exceptions.users.AccountLockedException;
import com.example.doantmdt.exceptions.users.InvalidCredentialsException;
import com.example.doantmdt.models.users.UserRegisterInfo;
import com.example.doantmdt.payload.JwtData;
import com.example.doantmdt.payload.ResponseBody;
import com.example.doantmdt.repository.RolesRepository;
import com.example.doantmdt.repository.UsersRepository;
import com.example.doantmdt.service.AccountService;
import com.example.doantmdt.service.MailService;
import com.example.doantmdt.utils.DateMapperUtils;
import com.example.doantmdt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IAccountService implements AccountService {

    private final RolesRepository rolesRepository;
    private final UsersRepository usersRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final DateMapperUtils dateMapperUtils;
    private final MailService mailService;

    public IAccountService(UsersRepository usersRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, DateMapperUtils dateMapperUtils, RolesRepository rolesRepository, MailService mailService) {
        this.usersRepository = usersRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.dateMapperUtils = dateMapperUtils;
        this.rolesRepository = rolesRepository;
        this.mailService = mailService;
    }

    public String generateVerifyCode() {
        String character = "0123456789";
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(character.length());
            password.append(character.charAt(randomIndex));
        }
        return password.toString();
    }

    @Override
    public JwtData loginUsers(String username, String password) {
        UsersEntity usersEntity = usersRepository.findUsersEntitiesByEmailOrUsername(username, username);
        if (usersEntity != null && passwordEncoder.matches(password, usersEntity.getPasswordHash())) {
            if (usersEntity.getActive()) {
                List<String> listRoleString = usersEntity.getRoles().stream()
                        .map(RoleEntity::getRoleName)
                        .collect(Collectors.toList());
                String jws = jwtUtils.generateTokens(username, listRoleString);
                return JwtData.builder()
                        .tokenType("Bearer ")
                        .accessToken(jws)
                        .refreshToken("")
                        .dob(dateMapperUtils.localDateToString(usersEntity.getDob()))
                        .fullName(usersEntity.getFirstName() + " " + usersEntity.getLastName())
                        .imageUrl(usersEntity.getImageUrl())
                        .email(usersEntity.getEmail())
                        .phoneNumber(usersEntity.getPhoneNumber())
                        .issuedAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusDays(3))
                        .role(listRoleString)
                        .build();
            } else {
                log.info("Account Locked!");
                throw new AccountLockedException("Account Locked!");
            }
        } else {
            log.info("Invalid Account!");
            throw new InvalidCredentialsException("Invalid Account!");
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public ResponseBody registerUsers(UserRegisterInfo user) {
        try {
            UsersEntity checkUsername = usersRepository.findUsersEntitiesByUsername(user.getUsername());
            if (checkUsername != null) {
                return new ResponseBody(null, ResponseBody.Status.SUCCESS, "USERNAME_EXISTED", ResponseBody.Code.SUCCESS);
            }
            UsersEntity checkEmail = usersRepository.findUsersEntitiesByEmail(user.getEmail());
            if (checkEmail != null && checkEmail.getEnable()) {
                return new ResponseBody(null, ResponseBody.Status.SUCCESS, "EMAIL_EXISTED", ResponseBody.Code.SUCCESS);
            } else{
                Optional<RoleEntity> roleOptional = rolesRepository.findByRoleName("KHACHHANG");
                List<RoleEntity> roleEntityList = new ArrayList<>();
                roleOptional.ifPresent(roleEntityList::add);
                UsersEntity usersEntity = new UsersEntity();
                String verifyCode = generateVerifyCode();
                usersEntity.setDob(dateMapperUtils.stringToLocalDate(user.getDob()));
                usersEntity.setFirstName(user.getFirstName());
                usersEntity.setLastName(user.getLastName());
                usersEntity.setEmail(user.getEmail());
                usersEntity.setPhoneNumber(user.getPhoneNumber());
                usersEntity.setEnable(false);
                usersEntity.setUsername(user.getUsername());
                usersEntity.setActive(true);
                usersEntity.setRoles(roleEntityList);
                usersEntity.setImageUrl("user.jpg");
                usersEntity.setVerifyCode(passwordEncoder.encode(verifyCode));
                usersEntity.setPasswordHash(passwordEncoder.encode(user.getPassword()));
                usersRepository.save(usersEntity);
                mailService.sendVerifyRegisterEmail(user.getEmail(), verifyCode);
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.info("Register account failed " + e.getMessage());
            throw new RequestNotFoundException("Register Failed!");
        }
    }

    @Transactional
    @Override
    public ResponseBody checkVerifyCodeRegister(String email, String verifyCode) {
        UsersEntity user = usersRepository.findUsersEntitiesByEmail(email);
        if (user != null) {
            if (passwordEncoder.matches(verifyCode, user.getVerifyCode())) {
                user.setEnable(true);
                user.setVerifyCode(null);
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                return new ResponseBody("", ResponseBody.Status.SUCCESS, "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
            }
        } else {
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "EMAIL_NOT_EXISTED", ResponseBody.Code.SUCCESS);
        }
    }


    @Transactional
    @Override
    public ResponseBody userForgotPassword(String email) {
        try {
            UsersEntity usersEntity = usersRepository.findUsersEntitiesByEmail(email);
            if (usersEntity != null) {
                String verifyCode = generateVerifyCode();
                usersEntity.setVerifyCode(passwordEncoder.encode(verifyCode));
                mailService.sendFogotPasswordMail(
                        usersEntity.getFirstName() + " " + usersEntity.getLastName(),
                        email,
                        verifyCode);
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else {
                return new ResponseBody("", ResponseBody.Status.SUCCESS, "USER_NOT_EXISTED", ResponseBody.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.warn("Forget Password Failed");
            throw new RequestNotFoundException("Error");
        }
    }

    @Transactional
    @Override
    public ResponseBody checkVerifyCodeForgotPassword(String email, String newPassword, String verifyCode) {
        UsersEntity usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity != null){
            if(passwordEncoder.matches(verifyCode, usersEntity.getVerifyCode())){
                usersEntity.setPasswordHash(passwordEncoder.encode(newPassword));
                return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
            } else{
                return new ResponseBody("", ResponseBody.Status.SUCCESS, "VERIFY_CODE_ERROR", ResponseBody.Code.SUCCESS);
            }
        } else{
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "EMAIL_NOT_EXISTED", ResponseBody.Code.SUCCESS);
        }
    }
}

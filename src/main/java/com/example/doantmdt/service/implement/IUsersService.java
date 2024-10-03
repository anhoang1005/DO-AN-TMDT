package com.example.doantmdt.service.implement;

import com.example.doantmdt.entities.UsersEntity;
import com.example.doantmdt.exceptions.ghn.RequestNotFoundException;
import com.example.doantmdt.exceptions.users.UnauthorizedException;
import com.example.doantmdt.mapper.users.UsersMapper;
import com.example.doantmdt.models.users.UserRegisterInfo;
import com.example.doantmdt.payload.ResponseBody;
import com.example.doantmdt.repository.UsersRepository;
import com.example.doantmdt.service.FileService;
import com.example.doantmdt.service.UsersService;
import com.example.doantmdt.utils.AuthenticationUtils;
import com.example.doantmdt.utils.DateMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class IUsersService implements UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationUtils authenticationUtils;
    private final DateMapperUtils dateMapperUtils;
    private final FileService fileService;
    public IUsersService(UsersRepository usersRepository, UsersMapper usersMapper, PasswordEncoder passwordEncoder, AuthenticationUtils authenticationUtils, DateMapperUtils dateMapperUtils, FileService fileService) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationUtils = authenticationUtils;
        this.dateMapperUtils = dateMapperUtils;
        this.fileService = fileService;
    }

    @Override
    @Transactional
    public ResponseBody usersChangePassword(String email, String oldPassword, String newPassword) {
        if(!authenticationUtils.checkUsersAuthentication(email)){
            throw new UnauthorizedException("Unauthorized!");
        }
        UsersEntity usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity!=null && passwordEncoder.matches(oldPassword, usersEntity.getPasswordHash())){
            usersEntity.setPasswordHash(passwordEncoder.encode(newPassword));
            usersRepository.save(usersEntity);
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } else{
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "INCORRECT_PASSWORD", ResponseBody.Code.SUCCESS);
        }
    }

    @Override
    @Transactional
    public ResponseBody usersChangeUserInfo(UserRegisterInfo info) {
        if(!authenticationUtils.checkUsersAuthentication(info.getEmail())){
            throw new UnauthorizedException("Unauthorized!");
        }
        UsersEntity checkExistedUsername = usersRepository.findUsersEntitiesByUsername(info.getUsername());
        if(checkExistedUsername!=null && !checkExistedUsername.getUsername().equals(info.getUsername())){
            return new ResponseBody("", ResponseBody.Status.SUCCESS, "EXISTED_USERNAME", ResponseBody.Code.SUCCESS);
        }
        UsersEntity user = usersRepository.findUsersEntitiesByEmail(info.getEmail());
        user.setUsername(info.getUsername());
        user.setPhoneNumber(info.getPhoneNumber());
        user.setDob(dateMapperUtils.stringToLocalDate(info.getDob()));
        user.setLastName(info.getLastName());
        user.setFirstName(info.getFirstName());
        usersRepository.save(user);
        return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
    }

    @Override
    @Transactional
    public ResponseBody usersChangeAvatar(String email, MultipartFile file) {
        if(!authenticationUtils.checkUsersAuthentication(email)){
            throw new UnauthorizedException("Unauthorized!");
        }
        UsersEntity usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity!=null){
            ResponseBody responseImage = fileService.uploadToCloudinary(file);
            usersEntity.setImageUrl(responseImage.getData().toString());
            usersRepository.save(usersEntity);
            return new ResponseBody("OK", ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } else{
            throw new RequestNotFoundException("Email not existed");
        }
    }

    @Override
    public ResponseBody usersGetUsersDetail(String email) {
        if(!authenticationUtils.checkUsersAuthentication(email)){
            throw new UnauthorizedException("Unauthorized!");
        }
        UsersEntity usersEntity = usersRepository.findUsersEntitiesByEmail(email);
        if(usersEntity!=null){
            return new ResponseBody(
                    usersMapper.entityToUsersDetail(usersEntity),
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS
            );
        } else{
            throw new RequestNotFoundException("Bad request");
        }
    }
}

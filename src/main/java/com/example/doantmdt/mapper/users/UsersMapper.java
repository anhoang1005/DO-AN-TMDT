package com.example.doantmdt.mapper.users;

import com.example.doantmdt.entities.UsersEntity;
import com.example.doantmdt.models.users.UsersDetail;
import com.example.doantmdt.utils.DateMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UsersMapper {
    private final DateMapperUtils dateMapperUtils;
    public UsersMapper(DateMapperUtils dateMapperUtils) {
        this.dateMapperUtils = dateMapperUtils;
    }


    public UsersDetail entityToUsersDetail(UsersEntity entity){
        return UsersDetail.builder()
                .userCode(entity.getUserCode())
                .imageUrl(entity.getImageUrl())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dob(dateMapperUtils.localDateToString(entity.getDob()))
                .phoneNumber(entity.getPhoneNumber())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .role(entity.getRoles().stream().map(role -> role.getRoleName()).collect(Collectors.toList()))
                .active(entity.getActive())
                .enable(entity.getEnable())
                .createdAt(dateMapperUtils.localDateTimeToString(entity.getCreatedAt()))
                .modifiedAt(dateMapperUtils.localDateTimeToString(entity.getModifiedAt()))
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}

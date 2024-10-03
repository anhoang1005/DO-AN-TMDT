package com.example.doantmdt.repository;

import com.example.doantmdt.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    UsersEntity findUsersEntitiesByUsername(String username);
    UsersEntity findUsersEntitiesByEmail(String email);
    UsersEntity findUsersEntitiesByEmailOrUsername(String email, String username);
}
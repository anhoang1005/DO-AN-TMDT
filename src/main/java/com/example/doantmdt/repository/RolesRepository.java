package com.example.doantmdt.repository;

import com.example.doantmdt.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String role);
}

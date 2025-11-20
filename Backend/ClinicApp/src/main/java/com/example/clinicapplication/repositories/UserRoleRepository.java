package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.UserRole;
import com.example.clinicapplication.models.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}

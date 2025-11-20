package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.MedicalCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, Long> {
    Optional<MedicalCondition> findByName(String name);
}

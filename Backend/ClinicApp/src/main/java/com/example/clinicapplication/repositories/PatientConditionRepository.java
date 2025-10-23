package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.PatientCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientConditionRepository extends JpaRepository<PatientCondition, Integer> {
}

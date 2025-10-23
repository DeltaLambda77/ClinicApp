package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.PatientCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientConditionRepository extends JpaRepository<PatientCondition, Integer> {
    Optional<PatientCondition> findByPatientIdAndConditionId(int patientId, int conditionId);
    void deleteByPatientIdAndConditionId(int patientId, int conditionId);
}

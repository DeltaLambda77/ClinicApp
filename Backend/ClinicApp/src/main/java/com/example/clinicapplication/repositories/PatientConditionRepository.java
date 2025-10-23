package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.PatientCondition;
import com.example.clinicapplication.models.PatientConditionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PatientConditionRepository extends JpaRepository<PatientCondition, PatientConditionId> {

    @Query("SELECT pc FROM PatientCondition pc WHERE pc.patient.patientId = :patientId AND pc.condition.conditionId = :conditionId")
    Optional<PatientCondition> findByPatientIdAndConditionId(@Param("patientId") Long patientId, @Param("conditionId") Long conditionId);

    @Transactional
    @Modifying
    @Query("DELETE FROM PatientCondition pc WHERE pc.patient.patientId = :patientId AND pc.condition.conditionId = :conditionId")
    void deleteByPatientIdAndConditionId(@Param("patientId") Long patientId, @Param("conditionId") Long conditionId);
}
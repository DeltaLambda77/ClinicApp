package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Keep existing methods
    List<Patient> findByLastName(String lastName);

    @Query(value = """
        SELECT p.* FROM Patient p
        JOIN PatientCondition pc ON p.PatientID = pc.PatientID
        JOIN MedicalCondition c ON c.ConditionID = pc.ConditionID
        WHERE c.Name = :conditionName
        """, nativeQuery = true)
    List<Patient> findPatientsByCondition(String conditionName);
}
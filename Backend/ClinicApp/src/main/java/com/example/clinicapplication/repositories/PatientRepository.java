package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    List<Patient> findByLastName(String lastName);

    @Query(value = """
        SELECT p.* FROM patient p
        JOIN patient_condition pc ON p.patient_id = pc.patient_id
        JOIN condition c ON c.condition_id = pc.condition_id
        WHERE c.name = :conditionName
        """, nativeQuery = true)
    List<Patient> findPatientsByCondition(String conditionName);
}

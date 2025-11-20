package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.PatientMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientMedicationRepository extends JpaRepository<PatientMedication, PatientMedicationId> {
}
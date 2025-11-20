package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<Medication> findByName(String name);
}
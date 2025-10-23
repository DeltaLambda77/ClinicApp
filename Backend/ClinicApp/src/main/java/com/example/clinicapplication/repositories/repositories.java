package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.Patient;
import com.example.clinicapplication.models.Trial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> { }

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> { }

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> { }

@Repository
public interface TrialRepository extends JpaRepository<Trial, Long> { }

@Repository
public interface TrialRequirementRepository extends JpaRepository<TrialRequirement, Long> { }

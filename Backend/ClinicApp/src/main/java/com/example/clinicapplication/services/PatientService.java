package com.example.clinicapplication.services;

import com.example.clinicapplication.models.Patient;
import com.example.clinicapplication.models.Trial;
import com.example.clinicapplication.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> findEligiblePatients(Trial trial) {
        return List.of();
    }
}

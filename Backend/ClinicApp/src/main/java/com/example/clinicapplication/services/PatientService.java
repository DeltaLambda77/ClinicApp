package com.example.clinicapplication.services;

import com.example.clinicapplication.dto.PatientDTO;
import com.example.clinicapplication.models.Patient;
import com.example.clinicapplication.repositories.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PatientDTO addPatient(Patient patient) {
        Patient saved = patientRepository.save(patient);
        return convertToDTO(saved);
    }

    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getSex() != null ? patient.getSex().name() : null,
                patient.getContactInfo()
        );
    }
}

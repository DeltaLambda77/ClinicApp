package com.example.clinicapplication.services;

import com.example.clinicapplication.models.Medication;
import com.example.clinicapplication.repositories.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Optional<Medication> getMedicationById(Long id) {
        return medicationRepository.findById(id);
    }

    public Medication addMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Long id, Medication updatedMedication) {
        return medicationRepository.findById(id).map(existing -> {
            existing.setName(updatedMedication.getName());
            existing.setDosage(updatedMedication.getDosage());
            return medicationRepository.save(existing);
        }).orElse(null);
    }

    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }
}
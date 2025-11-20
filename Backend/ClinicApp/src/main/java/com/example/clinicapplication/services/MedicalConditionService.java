package com.example.clinicapplication.services;

import com.example.clinicapplication.models.MedicalCondition;
import com.example.clinicapplication.repositories.MedicalConditionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalConditionService {
    private final MedicalConditionRepository medicalConditionRepository;

    public MedicalConditionService(MedicalConditionRepository medicalConditionRepository) {
        this.medicalConditionRepository = medicalConditionRepository;
    }

    public List<MedicalCondition> getAllMedicalConditions() {
        return medicalConditionRepository.findAll();
    }

    public Optional<MedicalCondition> getMedicalConditionById(Long id) {
        return medicalConditionRepository.findById(id);
    }

    public MedicalCondition addMedicalCondition(MedicalCondition medicalCondition) {
        return medicalConditionRepository.save(medicalCondition);
    }

    public MedicalCondition updateMedicalCondition(int id, MedicalCondition updatedMedicalCondition) {
        return medicalConditionRepository.findById(id).map(existing -> {
            existing.setName(updatedMedicalCondition.getName());
            existing.setCode(updatedMedicalCondition.getCode());
            existing.setDescription(updatedMedicalCondition.getDescription());
            return medicalConditionRepository.save(existing);
        }).orElse(null);
    }

    public void deleteMedicalCondition(int id) {
        medicalConditionRepository.deleteById(id);
    }
}

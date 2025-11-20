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

    public List<MedicalCondition> getAllConditions() {
        return medicalConditionRepository.findAll();
    }

    public Optional<MedicalCondition> getConditionById(Long id) {
        return medicalConditionRepository.findById(id);
    }

    public MedicalCondition addCondition(MedicalCondition condition) {
        return medicalConditionRepository.save(condition);
    }

    public MedicalCondition updateCondition(Long id, MedicalCondition updatedCondition) {
        return medicalConditionRepository.findById(id).map(existing -> {
            existing.setName(updatedCondition.getName());
            existing.setCode(updatedCondition.getCode());
            existing.setDescription(updatedCondition.getDescription());
            return medicalConditionRepository.save(existing);
        }).orElse(null);
    }

    public void deleteCondition(Long id) {
        medicalConditionRepository.deleteById(id);
    }
}
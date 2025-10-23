package com.example.clinicapplication.services;

import com.example.clinicapplication.models.PatientCondition;
import com.example.clinicapplication.repositories.PatientConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientConditionService {

    private final PatientConditionRepository patientConditionRepository;

    public PatientConditionService(PatientConditionRepository patientConditionRepository) {
        this.patientConditionRepository = patientConditionRepository;
    }

    public List<PatientCondition> getAllPatientConditions() {
        return patientConditionRepository.findAll();
    }

    public Optional<PatientCondition> getPatientConditionByIds(long patientId, long conditionId) {
        return patientConditionRepository.findByPatientIdAndConditionId(patientId, conditionId);
    }

    public PatientCondition addPatientCondition(PatientCondition pc) {
        return patientConditionRepository.save(pc);
    }

    public void deletePatientCondition(long patientId, long conditionId) {
        patientConditionRepository.deleteByPatientIdAndConditionId(patientId, conditionId);
    }
}

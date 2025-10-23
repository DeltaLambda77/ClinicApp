package com.example.clinicapplication.services;

import com.example.clinicapplication.models.Condition;
import com.example.clinicapplication.repositories.ConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConditionService {

    private final ConditionRepository conditionRepository;

    public ConditionService(ConditionRepository conditionRepository) {
        this.conditionRepository = conditionRepository;
    }

    public List<Condition> getAllConditions() {
        return conditionRepository.findAll();
    }

    public Optional<Condition> getConditionById(int id) {
        return conditionRepository.findById(id);
    }

    public Condition addCondition(Condition condition) {
        return conditionRepository.save(condition);
    }

    public Condition updateCondition(int id, Condition updatedCondition) {
        return conditionRepository.findById(id).map(existing -> {
            existing.setName(updatedCondition.getName());
            existing.setCode(updatedCondition.getCode());
            existing.setDescription(updatedCondition.getDescription());
            return conditionRepository.save(existing);
        }).orElse(null);
    }

    public void deleteCondition(int id) {
        conditionRepository.deleteById(id);
    }
}

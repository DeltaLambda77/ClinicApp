package com.example.clinicapplication.services;

import com.example.clinicapplication.models.Trial;
import com.example.clinicapplication.repositories.TrialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrialService {

    private final TrialRepository trialRepository;

    public TrialService(TrialRepository trialRepository) {
        this.trialRepository = trialRepository;
    }

    public List<Trial> getAllTrials() {
        return trialRepository.findAll();
    }

    public Optional<Trial> getTrialById(Long id) {
        return trialRepository.findById(id);
    }

    public Trial addTrial(Trial trial) {
        return trialRepository.save(trial);
    }

    public Trial updateTrial(Long id, Trial updatedTrial) {
        return trialRepository.findById(id).map(existing -> {
            existing.setTitle(updatedTrial.getTitle());
            existing.setMinimumAge(updatedTrial.getMinimumAge());
            existing.setMaximumAge(updatedTrial.getMaximumAge());
            existing.setStartDate(updatedTrial.getStartDate());
            existing.setEndDate(updatedTrial.getEndDate());
            return trialRepository.save(existing);
        }).orElse(null);
    }

    public void deleteTrial(Long id) {
        trialRepository.deleteById(id);
    }
}
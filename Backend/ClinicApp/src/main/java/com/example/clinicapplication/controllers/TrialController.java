package com.example.clinicapplication.controllers;

import com.example.clinicapplication.models.Trial;
import com.example.clinicapplication.services.TrialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trials")
public class TrialController {

    private final TrialService trialService;

    public TrialController(TrialService trialService) {
        this.trialService = trialService;
    }

    @GetMapping
    public List<Trial> getAllTrials() {
        System.out.println("GET /api/trials called");
        List<Trial> trials = trialService.getAllTrials();
        System.out.println("Returning " + trials.size() + " trials");
        return trials;
    }

    @GetMapping("/{id}")
    public Trial getTrialById(@PathVariable Long id) {
        return trialService.getTrialById(id).orElse(null);
    }
}
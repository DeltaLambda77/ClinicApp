package com.example.clinicapplication.controllers;

import com.example.clinicapplication.models.MedicalCondition;
import com.example.clinicapplication.services.MedicalConditionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conditions")
public class MedicalConditionController {

    private final MedicalConditionService conditionService;

    public MedicalConditionController(MedicalConditionService conditionService) {
        this.conditionService = conditionService;
    }

    @GetMapping
    public List<MedicalCondition> getAllConditions() {
        System.out.println("GET /api/conditions called");
        return conditionService.getAllConditions();
    }
}
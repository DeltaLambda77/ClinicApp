package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.TrialRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrialRequirementRepository extends JpaRepository<TrialRequirement, Integer> {

    List<TrialRequirement> findByTrialTrialID(Integer trialID);
}

package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.TrialRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrialRequirementRepository extends JpaRepository<TrialRequirement, Long> {
    @Query("SELECT tr FROM TrialRequirement tr WHERE tr.trial.trialId = :trialId")
    List<TrialRequirement> findByTrialTrialId(@Param("trialId") Long trialId);
}
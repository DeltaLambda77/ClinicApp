package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Integer> {

    Condition findByName(String name);
}

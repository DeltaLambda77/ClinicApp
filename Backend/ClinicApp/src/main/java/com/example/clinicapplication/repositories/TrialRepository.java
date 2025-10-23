package com.example.clinicapplication.repositories;

import com.example.clinicapplication.models.Trial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrialRepository extends JpaRepository<Trial, Integer> {

    // Example: find trials within a date range
    @Query("SELECT t FROM Trial t WHERE t.startDate >= :start AND t.endDate <= :end")
    List<Trial> findTrialsBetweenDates(java.time.LocalDate start, java.time.LocalDate end);
}

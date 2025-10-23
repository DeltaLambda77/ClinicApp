package com.example.clinicapplication.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PatientConditionId implements Serializable {
    private Long patientId;
    private Long conditionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientConditionId that = (PatientConditionId) o;
        return conditionId == that.conditionId &&
                Objects.equals(patientId, that.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, conditionId);
    }
}

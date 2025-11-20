package com.example.clinicapplication.models;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PatientMedicationId implements Serializable {
    private Long patientId;
    private Long medicationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientMedicationId that = (PatientMedicationId) o;
        return Objects.equals(patientId, that.patientId) &&
                Objects.equals(medicationId, that.medicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, medicationId);
    }
}

package com.example.clinicapplication.controllers;

import com.example.clinicapplication.dto.PatientDTO;
import com.example.clinicapplication.models.Patient;
import com.example.clinicapplication.repositories.PatientConditionRepository;
import com.example.clinicapplication.repositories.PatientMedicationRepository;
import com.example.clinicapplication.repositories.PatientRepository;
import com.example.clinicapplication.services.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final PatientConditionRepository patientConditionRepository;
    private final PatientMedicationRepository patientMedicationRepository;

    public PatientController(PatientService patientService,
                             PatientRepository patientRepository,
                             PatientConditionRepository patientConditionRepository,
                             PatientMedicationRepository patientMedicationRepository) {
        this.patientService = patientService;
        this.patientRepository = patientRepository;
        this.patientConditionRepository = patientConditionRepository;
        this.patientMedicationRepository = patientMedicationRepository;
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        System.out.println("GET /api/patients called");
        List<PatientDTO> patients = patientService.getAllPatients();
        System.out.println("Returning " + patients.size() + " patients");
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        System.out.println("GET /api/patients/" + id + " called");
        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isPresent()) {
            PatientDTO dto = convertToDTO(patient.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody Patient patient) {
        System.out.println("POST /api/patients called");
        System.out.println("Creating patient: " + patient.getFirstName() + " " + patient.getLastName());

        try {
            PatientDTO created = patientService.addPatient(patient);
            System.out.println("Patient created with ID: " + created.getPatientId());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            System.err.println("Error creating patient: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        System.out.println("PUT /api/patients/" + id + " called");
        System.out.println("Updating patient: " + patient.getFirstName() + " " + patient.getLastName());

        try {
            Optional<Patient> existingPatient = patientRepository.findById(id);
            if (existingPatient.isEmpty()) {
                System.err.println("Patient not found with ID: " + id);
                return ResponseEntity.notFound().build();
            }

            Patient patientToUpdate = existingPatient.get();
            patientToUpdate.setFirstName(patient.getFirstName());
            patientToUpdate.setLastName(patient.getLastName());
            patientToUpdate.setDateOfBirth(patient.getDateOfBirth());
            patientToUpdate.setSex(patient.getSex());
            patientToUpdate.setContactInfo(patient.getContactInfo());

            Patient updated = patientRepository.save(patientToUpdate);
            PatientDTO dto = convertToDTO(updated);

            System.out.println("Patient updated successfully: " + id);
            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            System.err.println("Error updating patient: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        System.out.println("DELETE /api/patients/" + id + " called");

        try {
            if (!patientRepository.existsById(id)) {
                System.err.println("Patient not found with ID: " + id);
                return ResponseEntity.notFound().build();
            }

            // Manually delete related records first
            System.out.println("Deleting patient conditions for patient: " + id);
            patientConditionRepository.deleteAll(
                    patientConditionRepository.findAll().stream()
                            .filter(pc -> pc.getPatient().getPatientId().equals(id))
                            .toList()
            );

            System.out.println("Deleting patient medications for patient: " + id);
            patientMedicationRepository.deleteAll(
                    patientMedicationRepository.findAll().stream()
                            .filter(pm -> pm.getPatient().getPatientId().equals(id))
                            .toList()
            );

            // Now delete the patient
            System.out.println("Deleting patient: " + id);
            patientRepository.deleteById(id);
            patientRepository.flush();

            // Verify deletion
            boolean stillExists = patientRepository.existsById(id);
            System.out.println("Patient still exists after delete: " + stillExists);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private PatientDTO convertToDTO(Patient patient) {
        return new PatientDTO(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getSex() != null ? patient.getSex().name() : null,
                patient.getContactInfo()
        );
    }
}
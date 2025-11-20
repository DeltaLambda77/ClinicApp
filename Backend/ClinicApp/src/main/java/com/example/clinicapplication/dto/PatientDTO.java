package com.example.clinicapplication.dto;

import com.example.clinicapplication.models.Patient;
import java.time.LocalDate;

public class PatientDTO {
    private Long patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String sex;
    private String contactInfo;

    // Constructors
    public PatientDTO() {}

    public PatientDTO(Long patientId, String firstName, String lastName,
                      LocalDate dateOfBirth, String sex, String contactInfo) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}

package com.example.clinicapplication.config;

import com.example.clinicapplication.models.*;
import com.example.clinicapplication.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseSeeder {

    @Bean
    @Transactional
    public CommandLineRunner loadData(
            MedicalConditionRepository conditionRepo,
            MedicationRepository medicationRepo,
            PatientRepository patientRepo,
            TrialRepository trialRepo,
            TrialRequirementRepository trialReqRepo,
            PatientConditionRepository patientConditionRepo,
            PatientMedicationRepository patientMedicationRepo,
            RoleRepository roleRepo,
            UserRepository userRepo,
            UserRoleRepository userRoleRepo) {

        return args -> {
            System.out.println("🌱 Starting database seeding...");

            // Clear existing data if needed (optional)
            // patientMedicationRepo.deleteAll();
            // patientConditionRepo.deleteAll();
            // ... etc

            // Check if data already exists
            if (patientRepo.count() > 0) {
                System.out.println("✅ Database already seeded. Skipping...");
                return;
            }

            // PART 1: MEDICAL CONDITIONS
            System.out.println("📋 Seeding Medical Conditions...");
            Map<String, MedicalCondition> conditions = new HashMap<>();

            conditions.put("Type 2 Diabetes", conditionRepo.save(MedicalCondition.builder()
                    .name("Type 2 Diabetes").code("E11")
                    .description("Non-insulin-dependent diabetes mellitus").build()));

            conditions.put("Hypertension", conditionRepo.save(MedicalCondition.builder()
                    .name("Hypertension").code("I10")
                    .description("Essential (primary) hypertension").build()));

            conditions.put("Asthma", conditionRepo.save(MedicalCondition.builder()
                    .name("Asthma").code("J45")
                    .description("Chronic inflammatory disease of airways").build()));

            conditions.put("Rheumatoid Arthritis", conditionRepo.save(MedicalCondition.builder()
                    .name("Rheumatoid Arthritis").code("M05")
                    .description("Autoimmune disorder affecting joints").build()));

            conditions.put("Major Depressive Disorder", conditionRepo.save(MedicalCondition.builder()
                    .name("Major Depressive Disorder").code("F32")
                    .description("Persistent feeling of sadness and loss of interest").build()));

            conditions.put("Chronic Kidney Disease", conditionRepo.save(MedicalCondition.builder()
                    .name("Chronic Kidney Disease").code("N18")
                    .description("Progressive loss of kidney function").build()));

            conditions.put("Coronary Artery Disease", conditionRepo.save(MedicalCondition.builder()
                    .name("Coronary Artery Disease").code("I25")
                    .description("Damage to heart blood vessels").build()));

            conditions.put("COPD", conditionRepo.save(MedicalCondition.builder()
                    .name("COPD").code("J44")
                    .description("Chronic Obstructive Pulmonary Disease").build()));

            conditions.put("Migraine", conditionRepo.save(MedicalCondition.builder()
                    .name("Migraine").code("G43")
                    .description("Recurrent severe headaches").build()));

            conditions.put("Osteoarthritis", conditionRepo.save(MedicalCondition.builder()
                    .name("Osteoarthritis").code("M19")
                    .description("Degenerative joint disease").build()));

            System.out.println("✅ Seeded " + conditions.size() + " conditions");

            // PART 2: MEDICATIONS
            System.out.println("💊 Seeding Medications...");
            Map<String, Medication> medications = new HashMap<>();

            medications.put("Metformin", medicationRepo.save(Medication.builder()
                    .name("Metformin").dosage("500mg twice daily").build()));

            medications.put("Lisinopril", medicationRepo.save(Medication.builder()
                    .name("Lisinopril").dosage("10mg once daily").build()));

            medications.put("Albuterol", medicationRepo.save(Medication.builder()
                    .name("Albuterol").dosage("90mcg as needed").build()));

            medications.put("Methotrexate", medicationRepo.save(Medication.builder()
                    .name("Methotrexate").dosage("15mg weekly").build()));

            medications.put("Sertraline", medicationRepo.save(Medication.builder()
                    .name("Sertraline").dosage("50mg once daily").build()));

            medications.put("Atorvastatin", medicationRepo.save(Medication.builder()
                    .name("Atorvastatin").dosage("20mg once daily").build()));

            medications.put("Prednisone", medicationRepo.save(Medication.builder()
                    .name("Prednisone").dosage("5mg once daily").build()));

            medications.put("Insulin Glargine", medicationRepo.save(Medication.builder()
                    .name("Insulin Glargine").dosage("20 units at bedtime").build()));

            medications.put("Aspirin", medicationRepo.save(Medication.builder()
                    .name("Aspirin").dosage("81mg once daily").build()));

            medications.put("Ibuprofen", medicationRepo.save(Medication.builder()
                    .name("Ibuprofen").dosage("400mg as needed").build()));

            medications.put("Warfarin", medicationRepo.save(Medication.builder()
                    .name("Warfarin").dosage("5mg once daily").build()));

            medications.put("Levothyroxine", medicationRepo.save(Medication.builder()
                    .name("Levothyroxine").dosage("100mcg once daily").build()));

            System.out.println("✅ Seeded " + medications.size() + " medications");

            // PART 3: PATIENTS
            System.out.println("👥 Seeding Patients...");
            Patient[] patients = new Patient[15];

            patients[0] = patientRepo.save(Patient.builder()
                    .firstName("Maria").lastName("Garcia").dateOfBirth(LocalDate.of(1965, 3, 15))
                    .sex(Patient.Sex.F).contactInfo("maria.garcia@email.com").build());

            patients[1] = patientRepo.save(Patient.builder()
                    .firstName("James").lastName("Wilson").dateOfBirth(LocalDate.of(1958, 7, 22))
                    .sex(Patient.Sex.M).contactInfo("james.wilson@email.com").build());

            patients[2] = patientRepo.save(Patient.builder()
                    .firstName("Sarah").lastName("Chen").dateOfBirth(LocalDate.of(1972, 11, 8))
                    .sex(Patient.Sex.F).contactInfo("sarah.chen@email.com").build());

            patients[3] = patientRepo.save(Patient.builder()
                    .firstName("Robert").lastName("Johnson").dateOfBirth(LocalDate.of(1980, 5, 30))
                    .sex(Patient.Sex.M).contactInfo("robert.j@email.com").build());

            patients[4] = patientRepo.save(Patient.builder()
                    .firstName("Lisa").lastName("Martinez").dateOfBirth(LocalDate.of(1955, 12, 18))
                    .sex(Patient.Sex.F).contactInfo("lisa.m@email.com").build());

            patients[5] = patientRepo.save(Patient.builder()
                    .firstName("David").lastName("Brown").dateOfBirth(LocalDate.of(1968, 9, 25))
                    .sex(Patient.Sex.M).contactInfo("david.brown@email.com").build());

            patients[6] = patientRepo.save(Patient.builder()
                    .firstName("Jennifer").lastName("Lee").dateOfBirth(LocalDate.of(1975, 4, 12))
                    .sex(Patient.Sex.F).contactInfo("jennifer.lee@email.com").build());

            patients[7] = patientRepo.save(Patient.builder()
                    .firstName("Michael").lastName("Davis").dateOfBirth(LocalDate.of(1963, 8, 7))
                    .sex(Patient.Sex.M).contactInfo("michael.d@email.com").build());

            patients[8] = patientRepo.save(Patient.builder()
                    .firstName("Patricia").lastName("Anderson").dateOfBirth(LocalDate.of(1970, 2, 20))
                    .sex(Patient.Sex.F).contactInfo("patricia.a@email.com").build());

            patients[9] = patientRepo.save(Patient.builder()
                    .firstName("William").lastName("Taylor").dateOfBirth(LocalDate.of(1952, 6, 14))
                    .sex(Patient.Sex.M).contactInfo("william.t@email.com").build());

            patients[10] = patientRepo.save(Patient.builder()
                    .firstName("Emily").lastName("Rodriguez").dateOfBirth(LocalDate.of(1985, 10, 3))
                    .sex(Patient.Sex.F).contactInfo("emily.r@email.com").build());

            patients[11] = patientRepo.save(Patient.builder()
                    .firstName("Daniel").lastName("Martinez").dateOfBirth(LocalDate.of(1990, 1, 25))
                    .sex(Patient.Sex.M).contactInfo("daniel.m@email.com").build());

            patients[12] = patientRepo.save(Patient.builder()
                    .firstName("Jessica").lastName("Hernandez").dateOfBirth(LocalDate.of(1978, 7, 19))
                    .sex(Patient.Sex.F).contactInfo("jessica.h@email.com").build());

            patients[13] = patientRepo.save(Patient.builder()
                    .firstName("Christopher").lastName("Lopez").dateOfBirth(LocalDate.of(1966, 12, 8))
                    .sex(Patient.Sex.M).contactInfo("chris.lopez@email.com").build());

            patients[14] = patientRepo.save(Patient.builder()
                    .firstName("Amanda").lastName("Gonzalez").dateOfBirth(LocalDate.of(1982, 3, 28))
                    .sex(Patient.Sex.F).contactInfo("amanda.g@email.com").build());

            System.out.println("✅ Seeded " + patients.length + " patients");

            // PART 4: TRIALS
            System.out.println("🔬 Seeding Trials...");
            Trial[] trials = new Trial[8];

            trials[0] = trialRepo.save(Trial.builder()
                    .title("Novel Diabetes Treatment Study")
                    .minimumAge(40).maximumAge(75)
                    .startDate(LocalDate.of(2024, 1, 15))
                    .endDate(LocalDate.of(2025, 12, 31)).build());

            trials[1] = trialRepo.save(Trial.builder()
                    .title("Hypertension Control Trial")
                    .minimumAge(35).maximumAge(70)
                    .startDate(LocalDate.of(2024, 3, 1))
                    .endDate(LocalDate.of(2025, 6, 30)).build());

            trials[2] = trialRepo.save(Trial.builder()
                    .title("Asthma Management Research")
                    .minimumAge(18).maximumAge(65)
                    .startDate(LocalDate.of(2024, 2, 1))
                    .endDate(LocalDate.of(2025, 8, 31)).build());

            trials[3] = trialRepo.save(Trial.builder()
                    .title("Arthritis Pain Relief Study")
                    .minimumAge(45).maximumAge(80)
                    .startDate(LocalDate.of(2024, 4, 1))
                    .endDate(LocalDate.of(2026, 3, 31)).build());

            trials[4] = trialRepo.save(Trial.builder()
                    .title("Depression Treatment Comparison")
                    .minimumAge(25).maximumAge(60)
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2025, 12, 31)).build());

            trials[5] = trialRepo.save(Trial.builder()
                    .title("Cardiovascular Health Initiative")
                    .minimumAge(50).maximumAge(85)
                    .startDate(LocalDate.of(2024, 5, 1))
                    .endDate(LocalDate.of(2026, 4, 30)).build());

            trials[6] = trialRepo.save(Trial.builder()
                    .title("Kidney Disease Prevention Trial")
                    .minimumAge(40).maximumAge(75)
                    .startDate(LocalDate.of(2024, 6, 1))
                    .endDate(LocalDate.of(2025, 12, 31)).build());

            trials[7] = trialRepo.save(Trial.builder()
                    .title("Migraine Prevention Study")
                    .minimumAge(21).maximumAge(65)
                    .startDate(LocalDate.of(2024, 3, 15))
                    .endDate(LocalDate.of(2025, 9, 30)).build());

            System.out.println("✅ Seeded " + trials.length + " trials");

            // PART 5: TRIAL REQUIREMENTS
            System.out.println("📝 Seeding Trial Requirements...");

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[0]).condition(conditions.get("Type 2 Diabetes"))
                    .notes("Must have Type 2 Diabetes diagnosis").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[0]).excludedMedication(medications.get("Insulin Glargine"))
                    .notes("Cannot be currently using insulin therapy").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[1]).condition(conditions.get("Hypertension"))
                    .notes("Must have diagnosed hypertension").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[1]).excludedMedication(medications.get("Warfarin"))
                    .notes("Cannot be on blood thinners").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[2]).condition(conditions.get("Asthma"))
                    .notes("Must have confirmed asthma diagnosis").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[3]).condition(conditions.get("Rheumatoid Arthritis"))
                    .notes("Must have rheumatoid arthritis").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[3]).excludedMedication(medications.get("Prednisone"))
                    .notes("Cannot be on long-term corticosteroids").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[4]).condition(conditions.get("Major Depressive Disorder"))
                    .notes("Must have major depressive disorder diagnosis").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[5]).condition(conditions.get("Coronary Artery Disease"))
                    .notes("Must have coronary artery disease").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[6]).condition(conditions.get("Chronic Kidney Disease"))
                    .notes("Must have chronic kidney disease").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[6]).excludedMedication(medications.get("Ibuprofen"))
                    .notes("Cannot be taking NSAIDs regularly").build());

            trialReqRepo.save(TrialRequirement.builder()
                    .trial(trials[7]).condition(conditions.get("Migraine"))
                    .notes("Must experience chronic migraines").build());

            System.out.println("✅ Seeded trial requirements");

            // PART 6: PATIENT CONDITIONS
            System.out.println("🏥 Seeding Patient Conditions...");

            // Patient 1: Maria Garcia
            savePatientCondition(patientConditionRepo, patients[0], conditions.get("Type 2 Diabetes"),
                    LocalDate.of(2020, 5, 10));
            savePatientCondition(patientConditionRepo, patients[0], conditions.get("Hypertension"),
                    LocalDate.of(2019, 3, 15));

            // Patient 2: James Wilson
            savePatientCondition(patientConditionRepo, patients[1], conditions.get("Hypertension"),
                    LocalDate.of(2015, 7, 20));
            savePatientCondition(patientConditionRepo, patients[1], conditions.get("Coronary Artery Disease"),
                    LocalDate.of(2018, 11, 5));

            // Patient 3: Sarah Chen
            savePatientCondition(patientConditionRepo, patients[2], conditions.get("Asthma"),
                    LocalDate.of(2010, 9, 12));
            savePatientCondition(patientConditionRepo, patients[2], conditions.get("Migraine"),
                    LocalDate.of(2022, 1, 8));

            // Patient 4: Robert Johnson
            savePatientCondition(patientConditionRepo, patients[3], conditions.get("Type 2 Diabetes"),
                    LocalDate.of(2021, 3, 25));

            // Patient 5: Lisa Martinez
            savePatientCondition(patientConditionRepo, patients[4], conditions.get("Rheumatoid Arthritis"),
                    LocalDate.of(2016, 6, 18));
            savePatientCondition(patientConditionRepo, patients[4], conditions.get("Osteoarthritis"),
                    LocalDate.of(2020, 9, 30));

            // Patient 6: David Brown
            savePatientCondition(patientConditionRepo, patients[5], conditions.get("Hypertension"),
                    LocalDate.of(2017, 4, 12));
            savePatientCondition(patientConditionRepo, patients[5], conditions.get("Type 2 Diabetes"),
                    LocalDate.of(2019, 8, 22));

            // Patient 7: Jennifer Lee
            savePatientCondition(patientConditionRepo, patients[6], conditions.get("Major Depressive Disorder"),
                    LocalDate.of(2021, 2, 14));

            // Patient 8: Michael Davis
            savePatientCondition(patientConditionRepo, patients[7], conditions.get("COPD"),
                    LocalDate.of(2014, 11, 30));
            savePatientCondition(patientConditionRepo, patients[7], conditions.get("Hypertension"),
                    LocalDate.of(2016, 5, 17));

            // Patient 9: Patricia Anderson
            savePatientCondition(patientConditionRepo, patients[8], conditions.get("Chronic Kidney Disease"),
                    LocalDate.of(2022, 3, 10));
            savePatientCondition(patientConditionRepo, patients[8], conditions.get("Hypertension"),
                    LocalDate.of(2020, 7, 25));

            // Patient 10: William Taylor
            savePatientCondition(patientConditionRepo, patients[9], conditions.get("Coronary Artery Disease"),
                    LocalDate.of(2012, 9, 8));
            savePatientCondition(patientConditionRepo, patients[9], conditions.get("Type 2 Diabetes"),
                    LocalDate.of(2015, 12, 20));

            // Patient 11: Emily Rodriguez
            savePatientCondition(patientConditionRepo, patients[10], conditions.get("Asthma"),
                    LocalDate.of(2023, 1, 15));

            // Patient 12: Daniel Martinez
            savePatientCondition(patientConditionRepo, patients[11], conditions.get("Migraine"),
                    LocalDate.of(2022, 6, 30));

            // Patient 13: Jessica Hernandez
            savePatientCondition(patientConditionRepo, patients[12], conditions.get("Type 2 Diabetes"),
                    LocalDate.of(2020, 10, 5));
            savePatientCondition(patientConditionRepo, patients[12], conditions.get("Hypertension"),
                    LocalDate.of(2021, 3, 18));

            // Patient 14: Christopher Lopez
            savePatientCondition(patientConditionRepo, patients[13], conditions.get("Rheumatoid Arthritis"),
                    LocalDate.of(2019, 7, 22));

            // Patient 15: Amanda Gonzalez
            savePatientCondition(patientConditionRepo, patients[14], conditions.get("Major Depressive Disorder"),
                    LocalDate.of(2021, 11, 12));
            savePatientCondition(patientConditionRepo, patients[14], conditions.get("Migraine"),
                    LocalDate.of(2020, 4, 8));

            System.out.println("✅ Seeded patient conditions");

            // PART 7: PATIENT MEDICATIONS
            System.out.println("💉 Seeding Patient Medications...");

            // Patient 1: Maria Garcia
            savePatientMedication(patientMedicationRepo, patients[0], medications.get("Metformin"),
                    LocalDate.of(2020, 5, 15), null);
            savePatientMedication(patientMedicationRepo, patients[0], medications.get("Lisinopril"),
                    LocalDate.of(2019, 3, 20), null);

            // Patient 2: James Wilson
            savePatientMedication(patientMedicationRepo, patients[1], medications.get("Lisinopril"),
                    LocalDate.of(2015, 7, 25), null);
            savePatientMedication(patientMedicationRepo, patients[1], medications.get("Atorvastatin"),
                    LocalDate.of(2018, 11, 10), null);
            savePatientMedication(patientMedicationRepo, patients[1], medications.get("Aspirin"),
                    LocalDate.of(2018, 11, 10), null);
            savePatientMedication(patientMedicationRepo, patients[1], medications.get("Warfarin"),
                    LocalDate.of(2019, 2, 1), null);

            // Patient 3: Sarah Chen
            savePatientMedication(patientMedicationRepo, patients[2], medications.get("Albuterol"),
                    LocalDate.of(2010, 9, 15), null);

            // Patient 4: Robert Johnson
            savePatientMedication(patientMedicationRepo, patients[3], medications.get("Metformin"),
                    LocalDate.of(2021, 4, 1), null);

            // Patient 5: Lisa Martinez
            savePatientMedication(patientMedicationRepo, patients[4], medications.get("Methotrexate"),
                    LocalDate.of(2016, 6, 25), null);
            savePatientMedication(patientMedicationRepo, patients[4], medications.get("Prednisone"),
                    LocalDate.of(2020, 10, 1), null);

            // Patient 6: David Brown
            savePatientMedication(patientMedicationRepo, patients[5], medications.get("Lisinopril"),
                    LocalDate.of(2017, 4, 20), null);
            savePatientMedication(patientMedicationRepo, patients[5], medications.get("Metformin"),
                    LocalDate.of(2019, 9, 1), null);

            // Patient 7: Jennifer Lee
            savePatientMedication(patientMedicationRepo, patients[6], medications.get("Sertraline"),
                    LocalDate.of(2021, 2, 20), null);

            // Patient 8: Michael Davis
            savePatientMedication(patientMedicationRepo, patients[7], medications.get("Albuterol"),
                    LocalDate.of(2014, 12, 5), null);
            savePatientMedication(patientMedicationRepo, patients[7], medications.get("Lisinopril"),
                    LocalDate.of(2016, 5, 25), null);

            // Patient 9: Patricia Anderson
            savePatientMedication(patientMedicationRepo, patients[8], medications.get("Lisinopril"),
                    LocalDate.of(2020, 8, 1), null);
            savePatientMedication(patientMedicationRepo, patients[8], medications.get("Ibuprofen"),
                    LocalDate.of(2022, 3, 15), null);

            // Patient 10: William Taylor
            savePatientMedication(patientMedicationRepo, patients[9], medications.get("Insulin Glargine"),
                    LocalDate.of(2016, 1, 10), null);
            savePatientMedication(patientMedicationRepo, patients[9], medications.get("Atorvastatin"),
                    LocalDate.of(2012, 9, 15), null);
            savePatientMedication(patientMedicationRepo, patients[9], medications.get("Aspirin"),
                    LocalDate.of(2012, 9, 15), null);

            // Patient 11: Emily Rodriguez
            savePatientMedication(patientMedicationRepo, patients[10], medications.get("Albuterol"),
                    LocalDate.of(2023, 1, 20), null);

            // Patient 13: Jessica Hernandez
            savePatientMedication(patientMedicationRepo, patients[12], medications.get("Metformin"),
                    LocalDate.of(2020, 10, 10), null);
            savePatientMedication(patientMedicationRepo, patients[12], medications.get("Lisinopril"),
                    LocalDate.of(2021, 3, 25), null);

            // Patient 14: Christopher Lopez
            savePatientMedication(patientMedicationRepo, patients[13], medications.get("Methotrexate"),
                    LocalDate.of(2019, 8, 1), null);

            // Patient 15: Amanda Gonzalez
            savePatientMedication(patientMedicationRepo, patients[14], medications.get("Sertraline"),
                    LocalDate.of(2021, 11, 20), null);

            System.out.println("✅ Seeded patient medications");

            // PART 8: ROLES
            System.out.println("🔐 Seeding Roles...");

            Role adminRole = roleRepo.save(Role.builder()
                    .roleName("Admin")
                    .description("System administrator with full access")
                    .canAddPatients(true)
                    .canViewTrials(true)
                    .canManageTrials(true)
                    .canViewReports(true).build());

            Role researcherRole = roleRepo.save(Role.builder()
                    .roleName("Researcher")
                    .description("Clinical researcher who can manage trials and view reports")
                    .canAddPatients(false)
                    .canViewTrials(true)
                    .canManageTrials(true)
                    .canViewReports(true).build());

            Role clinicianRole = roleRepo.save(Role.builder()
                    .roleName("Clinician")
                    .description("Medical staff who can add patients and view trials")
                    .canAddPatients(true)
                    .canViewTrials(true)
                    .canManageTrials(false)
                    .canViewReports(true).build());

            Role analystRole = roleRepo.save(Role.builder()
                    .roleName("Data Analyst")
                    .description("Analyst who can view reports but not modify data")
                    .canAddPatients(false)
                    .canViewTrials(true)
                    .canManageTrials(false)
                    .canViewReports(true).build());

            Role patientRole = roleRepo.save(Role.builder()
                    .roleName("Patient")
                    .description("Patient who can view their own eligibility")
                    .canAddPatients(false)
                    .canViewTrials(true)
                    .canManageTrials(false)
                    .canViewReports(false).build());

            System.out.println("✅ Seeded 5 roles");

            // PART 9: USERS
            System.out.println("👤 Seeding Users...");

            User adminUser = userRepo.save(User.builder()
                    .username("admin")
                    .email("admin@clinicapp.com")
                    .passwordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye")
                    .firstName("Alice")
                    .lastName("Admin")
                    .isActive(true).build());

            User researcher1 = userRepo.save(User.builder()
                    .username("researcher1")
                    .email("dr.smith@clinicapp.com")
                    .passwordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye")
                    .firstName("John")
                    .lastName("Smith")
                    .isActive(true).build());

            User clinician1 = userRepo.save(User.builder()
                    .username("clinician1")
                    .email("dr.jones@clinicapp.com")
                    .passwordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye")
                    .firstName("Emily")
                    .lastName("Jones")
                    .isActive(true).build());

            User analyst1 = userRepo.save(User.builder()
                    .username("analyst1")
                    .email("analyst@clinicapp.com")
                    .passwordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye")
                    .firstName("David")
                    .lastName("Chen")
                    .isActive(true).build());

            User patient1 = userRepo.save(User.builder()
                    .username("patient1")
                    .email("maria.garcia@email.com")
                    .passwordHash("$2a$10$N9qo8uLOickgx2ZMRZoMye")
                    .firstName("Maria")
                    .lastName("Garcia")
                    .isActive(true).build());

            System.out.println("✅ Seeded 5 users");

            // PART 10: USER ROLES
            System.out.println("🔗 Seeding User Roles...");

            saveUserRole(userRoleRepo, adminUser, adminRole, null);
            saveUserRole(userRoleRepo, researcher1, researcherRole, adminUser);
            saveUserRole(userRoleRepo, clinician1, clinicianRole, adminUser);
            saveUserRole(userRoleRepo, analyst1, analystRole, adminUser);
            saveUserRole(userRoleRepo, patient1, patientRole, adminUser);

            System.out.println("✅ Seeded user roles");

            System.out.println("🎉 Database seeding completed successfully!");
            System.out.println("📊 Summary:");
            System.out.println("   - " + conditionRepo.count() + " Medical Conditions");
            System.out.println("   - " + medicationRepo.count() + " Medications");
            System.out.println("   - " + patientRepo.count() + " Patients");
            System.out.println("   - " + trialRepo.count() + " Trials");
            System.out.println("   - " + trialReqRepo.count() + " Trial Requirements");
            System.out.println("   - " + patientConditionRepo.count() + " Patient Conditions");
            System.out.println("   - " + patientMedicationRepo.count() + " Patient Medications");
            System.out.println("   - " + roleRepo.count() + " Roles");
            System.out.println("   - " + userRepo.count() + " Users");
            System.out.println("   - " + userRoleRepo.count() + " User Role Assignments");
        };
    }

    // Helper methods
    private void savePatientCondition(PatientConditionRepository repo, Patient patient,
                                      MedicalCondition condition, LocalDate diagnosisDate) {
        PatientConditionId id = new PatientConditionId(patient.getPatientId(), condition.getConditionId());
        PatientCondition pc = PatientCondition.builder()
                .id(id)
                .patient(patient)
                .condition(condition)
                .diagnosisDate(diagnosisDate)
                .build();
        repo.save(pc);
    }

    private void savePatientMedication(PatientMedicationRepository repo, Patient patient,
                                       Medication medication, LocalDate startDate, LocalDate endDate) {
        PatientMedicationId id = new PatientMedicationId(patient.getPatientId(), medication.getMedicationId());
        PatientMedication pm = PatientMedication.builder()
                .id(id)
                .patient(patient)
                .medication(medication)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        repo.save(pm);
    }

    private void saveUserRole(UserRoleRepository repo, User user, Role role, User assignedBy) {
        UserRoleId id = new UserRoleId(user.getUserId(), role.getRoleId());
        UserRole userRole = UserRole.builder()
                .id(id)
                .user(user)
                .role(role)
                .assignedBy(assignedBy)
                .build();
        repo.save(userRole);
    }
}

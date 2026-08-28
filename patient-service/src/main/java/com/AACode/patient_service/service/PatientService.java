package com.AACode.patient_service.service;

import com.AACode.patient_service.dto.PatientRequestDTO;
import com.AACode.patient_service.dto.PatientResponseDTO;
import com.AACode.patient_service.exception.EmailAlreadyExistsException;
import com.AACode.patient_service.exception.PatientNotFoundException;
import com.AACode.patient_service.mapper.PatientMapper;
import com.AACode.patient_service.model.Patient;
import com.AACode.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOS = patients.stream()
                .map(patient -> PatientMapper.toDTO(patient)).toList();

        return patientResponseDTOS;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email has already exists" + patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(
                PatientMapper.toModel(patientRequestDTO)
        );

        return PatientMapper.toDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id)
        );
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email has already exists" + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatePatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatePatient);
    }

}

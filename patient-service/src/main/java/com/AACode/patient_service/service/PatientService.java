package com.AACode.patient_service.service;

import com.AACode.patient_service.dto.PatientRequestDTO;
import com.AACode.patient_service.dto.PatientResponseDTO;
import com.AACode.patient_service.exception.EmailAlreadyExistsException;
import com.AACode.patient_service.mapper.PatientMapper;
import com.AACode.patient_service.model.Patient;
import com.AACode.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

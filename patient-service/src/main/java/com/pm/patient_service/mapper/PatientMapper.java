package com.pm.patient_service.mapper;


import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.model.Patient;

@Component
public class PatientMapper {
	
	public static PatientResponseDTO toDTO(Patient patient){
		PatientResponseDTO patientResponse= new PatientResponseDTO();
		patientResponse.setId(patient.getId().toString());
		patientResponse.setName(patient.getName());
		patientResponse.setEmail(patient.getEmail());
		patientResponse.setAddress(patient.getAddress());		
		patientResponse.setDateOfBirth(patient.getDateOfBirth().toString());
		
		return patientResponse;
		
	}
	
	public static Patient toPatient(PatientRequestDTO patientRequestDTO) {
		Patient patient= new Patient();
		patient.setName(patientRequestDTO.getName());
		patient.setEmail(patientRequestDTO.getEmail());
		patient.setAddress(patientRequestDTO.getAddress());
		patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
		patient.setRegistrationDate(LocalDate.parse(patientRequestDTO.getRegistered_date()));
		
		return patient;
	}
}

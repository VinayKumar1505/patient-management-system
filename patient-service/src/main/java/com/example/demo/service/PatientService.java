package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;

@Service
public class PatientService {
	PatientRepository patientRepository;
	
	public PatientService(PatientRepository patientRepository){
		this.patientRepository= patientRepository;
	}
	
	public List<PatientResponseDTO> getPatients(){
		List<Patient> patients= patientRepository.findAll();
		
		return patients.stream().map(PatientMapper::toDTO).toList();
		
	}
	
	public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
		Patient patient= patientRepository.save(PatientMapper.toPatient(patientRequestDTO));
		return PatientMapper.toDTO(patient);
	}
}

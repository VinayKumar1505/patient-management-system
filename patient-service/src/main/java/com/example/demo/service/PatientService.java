package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.exception.PatientAlreadyExistsException;
import com.example.demo.exception.PatientNotFoundException;
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
		if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
			throw new PatientAlreadyExistsException("the partient already exists with email"+ patientRequestDTO.getEmail());
		}
		Patient patient= patientRepository.save(PatientMapper.toPatient(patientRequestDTO));
		return PatientMapper.toDTO(patient);
	}
	
	public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
		
		Patient patient= patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundException("The parient doesn't exist with id"+ id));
		
		if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {//if we update email we should check if patient exists with that email other that current user
			throw new PatientAlreadyExistsException("the partient already exists with email"+ patientRequestDTO.getEmail());
		}
		
		patient.setEmail(patientRequestDTO.getEmail());
		patient.setName(patientRequestDTO.getName());
		patient.setAddress(patientRequestDTO.getAddress());
		patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
		
		Patient updatedPatient= patientRepository.save(patient);
		
		return PatientMapper.toDTO(updatedPatient);
		
	}
	
	public void deletePatient(UUID id) {
		patientRepository.deleteById(id);
	}
}

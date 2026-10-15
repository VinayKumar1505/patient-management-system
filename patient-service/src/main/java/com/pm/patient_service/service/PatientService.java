package com.pm.patient_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.PatientAlreadyExistsException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.kafka.KafkaProducer;

@Service
public class PatientService {
	PatientRepository patientRepository;
	BillingServiceGrpcClient billingServiceGrpcClient;
	KafkaProducer kafkaProducer;

	public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer){
		this.patientRepository= patientRepository;
		this.billingServiceGrpcClient = billingServiceGrpcClient;
		this.kafkaProducer = kafkaProducer;
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
		billingServiceGrpcClient.createAccount(patient.getId().toString(), patient.getName(), patient.getEmail());
		kafkaProducer.sendEvent(patient);
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

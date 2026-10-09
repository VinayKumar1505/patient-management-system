package com.example.demo.dto;

import com.example.demo.dto.validators.CreatePatientValidationGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class PatientRequestDTO {

	@NotNull(message="The name shouldn't be empty")
	private String name;
	
	@NotNull(message="The email shouldn't be empty")
	@Email(message="The email should be valid")
	private String email;
	
	@NotNull(message="The address shouldn't be empty")
	private String address;
	
	@NotNull(message="The date of birth shouldn't be empty")
	private String dateOfBirth;
	
	@NotNull(groups = CreatePatientValidationGroup.class, message="The registered date shouldn't be empty")
	private String registered_date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getRegistered_date() {
		return registered_date;
	}

	public void setRegistered_date(String registered_date) {
		this.registered_date = registered_date;
	}
	
}

package com.softwaretesting.paymentservice.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode @JsonIgnoreProperties(value = {
		"id" }, allowGetters = true)

public class Customer {
	
	@Id private UUID id;
	
	@NotBlank @Column(nullable = false) private String name;
	
	@NotBlank @Column(nullable = false, unique = true) private String phoneNr;
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhoneNr() {
		return phoneNr;
	}
	
	public void setPhoneNr(String phoneNr) {
		this.phoneNr = phoneNr;
	}
}

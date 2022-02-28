package com.softwaretesting.paymentservice.customer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service @AllArgsConstructor public class CustomerRegistrationService {
	
	private final CustomerRepository customerRepository;
	
	public void registerNewCustomer(CustomerRegistrationRequest request) {
		String phoneNr = request.getCustomer().getPhoneNr();
		Optional<Customer> customerOptional = customerRepository.selectCustomerByPhoneNumber(phoneNr);
		
		if (customerOptional.isPresent()) {
			if (request.getCustomer().equals(customerOptional.get())) {
				return;
			}
			throw new IllegalStateException(String.format("phone number [%s] is taken!", phoneNr));
		} else {
			request.getCustomer().setId(UUID.randomUUID());
			customerRepository.save(request.getCustomer());
		}
	}
	
}

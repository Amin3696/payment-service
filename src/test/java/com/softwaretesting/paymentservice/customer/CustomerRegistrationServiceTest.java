package com.softwaretesting.paymentservice.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CustomerRegistrationServiceTest {
	
	@Mock private CustomerRepository customerRepository;
	
	@Captor private ArgumentCaptor<Customer> customerArgumentCaptor;
	
	private CustomerRegistrationService serviceUnderTest;
	
	@BeforeEach void setUp() {
		MockitoAnnotations.initMocks(this);
		serviceUnderTest = new CustomerRegistrationService(customerRepository);
	}
	
	@Test void itShouldSaveNewCustomer() {
		// Given a phone number and customer
		String phoneNumber = "0098";
		Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
		
		//...request
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
		
		//...customerRepository return empty customer
		given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());
		
		// When
		serviceUnderTest.registerNewCustomer(request);
		
		// Then
		then(customerRepository).should().save(customerArgumentCaptor.capture());
		Customer captorValue = customerArgumentCaptor.getValue();
		
		assertThat(captorValue).isEqualToComparingFieldByField(customer);
	}
	@Test void itShouldSaveNewCustomerWhenIdIsNull() {
		// Given a phone number and customer
		String phoneNumber = "0098";
		Customer customer = new Customer(null, "Maryam", phoneNumber);
		
		//...request
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
		
		//...customerRepository return empty customer
		given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.empty());
		
		// When
		serviceUnderTest.registerNewCustomer(request);
		
		// Then
		then(customerRepository).should().save(customerArgumentCaptor.capture());
		Customer captorValue = customerArgumentCaptor.getValue();
		
		assertThat(captorValue).isEqualToIgnoringGivenFields(customer, "id");
		assertThat(captorValue.getId()).isNotNull();
	}
	
	@Test void itShouldNotSaveCustomerWhenCustomerExists() {
		// Given
		String phoneNumber = "0098";
		Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
		
		//...request
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
		
		//...customerRepository return empty customer
		given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.of(customer));
		
		// When
		serviceUnderTest.registerNewCustomer(request);
		
		// Then
		then(customerRepository).should(never()).save(any());
	
		/*then(customerRepository).should().selectCustomerByPhoneNumber(phoneNumber);
		then(customerRepository).shouldHaveNoMoreInteractions();*/
		
	}
	
	@Test void itShouldThrowWhenPhoneNrIsTaken() {
		// Given
		String phoneNumber = "0098";
		Customer customer = new Customer(UUID.randomUUID(), "Maryam", phoneNumber);
		Customer savedCustomer = new Customer(UUID.randomUUID(), "Amin", phoneNumber);
		
		
		//...request
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
		
		//...customerRepository return a customer
		given(customerRepository.selectCustomerByPhoneNumber(phoneNumber)).willReturn(Optional.of(savedCustomer));
		
		// When
		
		// Then
		assertThatThrownBy(() -> serviceUnderTest.registerNewCustomer(request)).isInstanceOf(
						IllegalStateException.class)
				.hasMessageContaining(String.format("phone number [%s] is taken!", phoneNumber));
		
		// Finally
		then(customerRepository).should(never()).save(any(Customer.class));
		
	}
	
}
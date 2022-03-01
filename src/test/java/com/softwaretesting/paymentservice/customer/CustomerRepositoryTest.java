package com.softwaretesting.paymentservice.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {"spring.jpa.properties.javax.persistence.validation.mode=none"})
class CustomerRepositoryTest {
	
	@Autowired private CustomerRepository underTest;
	
	@Test void itShouldSelectCustomerByPhoneNumber() {
		// Given
		UUID id = UUID.randomUUID();
		String phoneNumber = "123456";
		Customer customer = new Customer(id, "Abel", phoneNumber);
		
		// When
		underTest.save(customer);
		
		// Then
		Optional<Customer> optionalCustomer = underTest.selectCustomerByPhoneNumber(phoneNumber);
		assertThat(optionalCustomer)
				.isPresent()
				.hasValueSatisfying(c -> {
					assertThat(c).isEqualToComparingFieldByField(customer);
				});
	}
	@Test void itShouldSelectCustomerByPhoneNumberWhenPhoneNrDoesNotExists() {
		// Given
		String phoneNumber = "1111";
		
		// When
		Optional<Customer> customer = underTest.selectCustomerByPhoneNumber(phoneNumber);
		// Then
		assertThat(customer).isNotPresent();
		
	}
	
	@Test void itShouldSaveCustomer() {
		// Given
		UUID id = UUID.randomUUID();
		Customer customer = new Customer(id, "Ali", "656565");
		
		// When
		underTest.save(customer);
		
		// Then
		Optional<Customer> byId = underTest.findById(id);
		assertThat(byId).isPresent().hasValueSatisfying(c -> {
					/*assertThat(c.getId()).isEqualTo(id);
					assertThat(c.getName()).isEqualTo("Ali");
					assertThat(c.getPhoneNr()).isEqualTo("656565");*/
			assertThat(c).isEqualToComparingFieldByField(customer);
		});
	}
	
	@Test void itShouldNotSaveWhenNameIsNull() {
		// Given
		UUID id = UUID.randomUUID();
		Customer customer = new Customer(id, null, "656565");
		
		// When
		// Then
		assertThatThrownBy(() -> underTest.save(customer)).hasMessageContaining(
						"not-null property references a null or transient value : com.softwaretesting.paymentservice.customer.Customer.name;")
				.isInstanceOf(DataIntegrityViolationException.class);
	}
	
	@Test void itShouldNotSaveWhenPhoneNrIsNull() {
		// Given
		UUID id = UUID.randomUUID();
		Customer customer = new Customer(id, "ali", null);
		
		// When
		// Then
		assertThatThrownBy(() -> underTest.save(customer)).hasMessageContaining(
						"not-null property references a null or transient value : com.softwaretesting.paymentservice.customer.Customer.phoneNr")
				.isInstanceOf(DataIntegrityViolationException.class);
	}
}
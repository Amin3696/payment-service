package com.softwaretesting.paymentservice.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(properties = {
		"spring.jpa.properties.javax.persistence.validation.mode=none" }) class PaymentRepositoryTest {
	@Autowired private PaymentRepository repositoryUnderTest;
	
	@Test void itShouldInsertPayment() {
		// Given
		
		Payment payment = new Payment(1L, UUID.randomUUID(), new BigDecimal(10.00), Currency.EURO, "visa123",
				"Donation");
		
		// When
		repositoryUnderTest.save(payment);
		
		// Then
		Optional<Payment> byId = repositoryUnderTest.findById(1L);
		assertThat(byId).isPresent()
				.hasValueSatisfying(p -> {
			assertThat(p).isEqualTo(payment);
		});
	}
}
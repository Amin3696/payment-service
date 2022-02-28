package com.softwaretesting.paymentservice.payment;

import com.softwaretesting.paymentservice.customer.Customer;
import com.softwaretesting.paymentservice.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class PaymentServiceTest {
	
	@Mock private CustomerRepository customerRepository;
	@Mock private PaymentRepository paymentRepository;
	@Mock private CardPaymentCharger cardPaymentCharger;
	
	private PaymentService paymentServiceUnderTest;
	
	@BeforeEach void setUp() {
		MockitoAnnotations.initMocks(this);
		paymentServiceUnderTest = new PaymentService(paymentRepository, customerRepository,cardPaymentCharger);
	}
	
	@Test void itShouldChargeCardSuccessfully() {
		// Given
		UUID customerID=UUID.randomUUID();
		
		//...customer exists
		given(customerRepository.findById(customerID)).willReturn(Optional.of(Mockito.mock(Customer.class)));
		
		//...payment request
		PaymentRequest paymentRequest=new PaymentRequest(
				new Payment(
						null,
						customerID,
						new BigDecimal("100.00"),
						Currency.USD,
						"card123xx",
						"Donation"));
		
		//... card is charged successfully
		given(cardPaymentCharger.chargeCard(
				paymentRequest.getPayment().getSource(),
				paymentRequest.getPayment().getAmount(),
				paymentRequest.getPayment().getCurrency(),
				paymentRequest.getPayment().getDescription()
		)).willReturn(new CardPaymentCharge(true));
		// When
		paymentServiceUnderTest.chargeCard(customerID,paymentRequest);
		
		// Then
		ArgumentCaptor<Payment> paymentArgumentCaptor=ArgumentCaptor.forClass(Payment.class);
		
		then(paymentRepository).should().save(paymentArgumentCaptor.capture());
		
		Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();
		
		assertThat(paymentArgumentCaptorValue)
				.isEqualToIgnoringGivenFields(
						paymentRequest.getPayment(),
						"customerID");
		
		assertThat(paymentArgumentCaptorValue.getCustomerId()).isEqualTo(customerID);
		
	}
}
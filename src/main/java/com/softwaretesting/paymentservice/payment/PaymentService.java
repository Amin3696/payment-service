package com.softwaretesting.paymentservice.payment;

import com.softwaretesting.paymentservice.customer.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
	public class PaymentService {
		
		private static final List<Currency> AcceptedCurrency = List.of(Currency.USD, Currency.EURO, Currency.GBP);
		
		private final PaymentRepository paymentRepository;
		private final CustomerRepository customerRepository;
		private final CardPaymentCharger cardPaymentCharger;
	
	
	void chargeCard(UUID customerId, PaymentRequest paymentRequest) {
			//1. Does customer exists if not throw
			boolean isCustomerFound = customerRepository.findById(customerId).isPresent();
			if (!isCustomerFound) {
				throw new IllegalStateException(String.format("customer with id [%s] not found", customerId));
			}
			
			//2. Do we Support the Currency if not throw
			boolean isCurrencySupported = AcceptedCurrency.stream()
					.anyMatch(c -> c.equals(paymentRequest.getPayment().getCurrency()));
			
			if (!isCurrencySupported) {
				String message = String.format("Currency[%S] is not supported.", paymentRequest.getPayment().getCurrency());
				throw new IllegalStateException(message);
			}
			
			//3. charge card
			CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(paymentRequest.getPayment().getSource(),
					paymentRequest.getPayment().getAmount(), paymentRequest.getPayment().getCurrency(),
					paymentRequest.getPayment().getDescription());
			
			//4. if not debited throw
			if (!cardPaymentCharge.isCardDebited()) {
				throw new IllegalStateException(String.format("Card not debited for customer %s", customerId));
			}
			//5. Insert payment
			paymentRequest.getPayment().setCustomerId(customerId);
			paymentRepository.save(paymentRequest.getPayment());
			
			//6. TODO: send sms
		}
}

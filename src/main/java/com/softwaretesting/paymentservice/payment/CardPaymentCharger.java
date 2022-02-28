package com.softwaretesting.paymentservice.payment;

import java.math.BigDecimal;

public interface CardPaymentCharger {
	
	CardPaymentCharge chargeCard(
			String cardSource,
			BigDecimal amount,
			Currency currency,
			String description
	);
}

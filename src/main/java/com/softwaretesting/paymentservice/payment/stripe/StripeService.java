package com.softwaretesting.paymentservice.payment.stripe;

import com.softwaretesting.paymentservice.payment.CardPaymentCharge;
import com.softwaretesting.paymentservice.payment.CardPaymentCharger;
import com.softwaretesting.paymentservice.payment.Currency;
import com.stripe.net.RequestOptions;

import java.math.BigDecimal;

public class StripeService implements CardPaymentCharger {
	private final RequestOptions requestOptions = RequestOptions.builder().setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc")
			.build();
	
	@Override public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency,
			String description) {
		return null;
	}
}

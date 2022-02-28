package com.softwaretesting.paymentservice.payment.stripe;

import com.softwaretesting.paymentservice.payment.CardPaymentCharge;
import com.softwaretesting.paymentservice.payment.CardPaymentCharger;
import com.softwaretesting.paymentservice.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StripeService implements CardPaymentCharger {
	private final RequestOptions requestOptions = RequestOptions.builder().setApiKey("sk_test_4eC39HqLyjWDarjtT1zdp7dc")
			.build();
	
	@Override public CardPaymentCharge chargeCard(String cardSource, BigDecimal amount, Currency currency,
			String description) {
		Map<String, Object> params = new HashMap<>();
		params.put("amount", amount);
		params.put("currency", currency);
		params.put("source", cardSource);
		params.put("description", description);
		
		try {
			Charge charge = Charge.create(params,requestOptions);
			return new CardPaymentCharge(charge.getPaid());
		} catch (StripeException e) {
			throw new IllegalStateException("can not make stripe charge",e);
		}
	}
}

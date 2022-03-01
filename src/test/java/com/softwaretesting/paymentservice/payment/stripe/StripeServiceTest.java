package com.softwaretesting.paymentservice.payment.stripe;

import com.softwaretesting.paymentservice.payment.CardPaymentCharge;
import com.softwaretesting.paymentservice.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class StripeServiceTest {
	private StripeService underTest;
	@Mock private StripeApi stripeApi;
	
	@BeforeEach void setUp() {
		MockitoAnnotations.initMocks(this);
		underTest = new StripeService(stripeApi);
	}
	
	@Test void itShouldChargeCard() throws StripeException {
		// Given
		String cardSource = "1x2x3x4";
		BigDecimal amount = new BigDecimal(100.00);
		Currency currency = Currency.USD;
		String description = "donation";
		
		Charge charge = new Charge();
		charge.setPaid(true);
		given(stripeApi.create(anyMap(), any())).willReturn(charge);
		
		// When
		CardPaymentCharge cardPaymentCharge = underTest.chargeCard(cardSource, amount, currency, description);
		
		// Then
		ArgumentCaptor<Map<String, Object>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
		ArgumentCaptor<RequestOptions> requestOptionsArgumentCaptor = ArgumentCaptor.forClass(RequestOptions.class);
		
		then(stripeApi).should().create(mapArgumentCaptor.capture(), requestOptionsArgumentCaptor.capture());
		
		Map<String, Object> requestMap = mapArgumentCaptor.getValue();
		
		assertThat(requestMap.keySet()).hasSize(4);
		
		assertThat(requestMap.get("amount")).isEqualTo(amount);
		assertThat(requestMap.get("currency")).isEqualTo(currency);
		assertThat(requestMap.get("source")).isEqualTo(cardSource);
		assertThat(requestMap.get("description")).isEqualTo(description);
		
		RequestOptions options = requestOptionsArgumentCaptor.getValue();
		assertThat(options).isNotNull();
		
		assertThat(cardPaymentCharge.isCardDebited()).isTrue();
	}
}

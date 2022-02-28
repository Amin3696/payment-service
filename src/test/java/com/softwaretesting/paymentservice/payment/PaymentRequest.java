package com.softwaretesting.paymentservice.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PaymentRequest {
	private final Payment payment;
	
	public PaymentRequest(@JsonProperty("payment") Payment payment) {
		this.payment = payment;
	}
	
	
}

package com.jsp.E_Banking.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PaymentFailedException extends RuntimeException {
	private String message;
}

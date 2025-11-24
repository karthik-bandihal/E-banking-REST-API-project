package com.jsp.E_Banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BankBalanceDto {
	private Long accountNumber;
	private Double balance;
}
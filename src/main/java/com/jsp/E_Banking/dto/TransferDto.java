package com.jsp.E_Banking.dto;

import lombok.Data;

@Data
public class TransferDto {
	private Double amount;
	private Long toAccountNumber;
}
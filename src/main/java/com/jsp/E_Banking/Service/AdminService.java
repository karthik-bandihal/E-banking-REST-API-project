package com.jsp.E_Banking.Service;

import org.springframework.http.ResponseEntity;

import com.jsp.E_Banking.dto.ResponseDto;

public interface AdminService {

	ResponseEntity<ResponseDto> getPendingAccounts();

	ResponseEntity<ResponseDto> getUser(Long accountNumber);

	ResponseEntity<ResponseDto> approveBankAccount(Long accountNumber);

}

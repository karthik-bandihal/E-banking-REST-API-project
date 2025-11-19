package com.jsp.E_Banking.Service;

import java.security.Principal;

import org.springframework.http.ResponseEntity;

import com.jsp.E_Banking.dto.LoginDto;
import com.jsp.E_Banking.dto.OtpDto;
import com.jsp.E_Banking.dto.ResetPasswordDto;
import com.jsp.E_Banking.dto.ResponseDto;
import com.jsp.E_Banking.dto.SavingAccountDto;
import com.jsp.E_Banking.dto.UserDto;

import jakarta.validation.Valid;

public interface UserService {

	ResponseEntity<ResponseDto> register(UserDto dto);

	ResponseEntity<ResponseDto> verifyOtp(OtpDto dto);

	ResponseEntity<ResponseDto> resendOtp(String email);

	ResponseEntity<ResponseDto> forgotPassword(String email);

	ResponseEntity<ResponseDto> resetPassword(ResetPasswordDto dto);

	ResponseEntity<ResponseDto> login(@Valid LoginDto dto);
	
	ResponseEntity<ResponseDto> viewSavingsAccount(Principal principal);

	ResponseEntity<ResponseDto> createSavingsAccount(Principal principal, SavingAccountDto accountDto);

}
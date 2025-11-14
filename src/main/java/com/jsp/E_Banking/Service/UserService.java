package com.jsp.E_Banking.Service;

import org.springframework.http.ResponseEntity;

import com.jsp.E_Banking.dto.ResponseDto;
import com.jsp.E_Banking.dto.UserDto;

public interface UserService {

	ResponseEntity<ResponseDto> register(UserDto dto);

	String check(String email);
}
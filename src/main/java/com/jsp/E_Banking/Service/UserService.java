package com.jsp.E_Banking.Service;

import org.springframework.http.ResponseEntity;
import com.jsp.E_Banking.Entity.TempUser;
import com.jsp.E_Banking.dto.UserDto;

public interface UserService {

	ResponseEntity<TempUser> register(UserDto dto);

	ResponseEntity<TempUser> fetch(String email);

}
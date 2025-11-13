package com.jsp.E_Banking.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.E_Banking.Entity.TempUser;
import com.jsp.E_Banking.Service.UserService;
import com.jsp.E_Banking.dto.UserDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<TempUser> register(@RequestBody @Valid UserDto dto) {
		return userService.register(dto);
	}

	@GetMapping("/verify/{email}")
	public ResponseEntity<TempUser> fetch(@PathVariable String email) {
		return userService.fetch(email);
	}
}
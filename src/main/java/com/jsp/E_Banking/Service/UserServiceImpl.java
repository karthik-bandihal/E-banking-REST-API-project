package com.jsp.E_Banking.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.LinkedHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jsp.E_Banking.Entity.SavingBankAccount;
import com.jsp.E_Banking.Entity.User;
import com.jsp.E_Banking.Exception.DataExistsException;
import com.jsp.E_Banking.Exception.DataNotFoundException;
import com.jsp.E_Banking.Exception.ExpiredException;
import com.jsp.E_Banking.Exception.MissMatchException;
import com.jsp.E_Banking.Repository.SavingAccountRepository;
import com.jsp.E_Banking.Repository.UserRepository;
import com.jsp.E_Banking.dto.BankingRole;
import com.jsp.E_Banking.dto.LoginDto;
import com.jsp.E_Banking.dto.OtpDto;
import com.jsp.E_Banking.dto.ResetPasswordDto;
import com.jsp.E_Banking.dto.ResponseDto;
import com.jsp.E_Banking.dto.SavingAccountDto;
import com.jsp.E_Banking.dto.UserDto;
import com.jsp.E_Banking.mapper.SavingsBankMapper;
import com.jsp.E_Banking.mapper.UserMapper;
import com.jsp.E_Banking.util.JwtUtil;
import com.jsp.E_Banking.util.MessageSendingHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final RedisService redisService;
	private final UserRepository userRepository;
	private final MessageSendingHelper messageSendingHelper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final SavingAccountRepository savingAccountRepository;
	private final UserMapper userMapper;
	private final SavingsBankMapper bankMapper;

	public ResponseEntity<ResponseDto> register(UserDto dto) {
		if (redisService.fetchUserDto(dto.getEmail()) == null) {
			if (!userRepository.existsByEmailOrMobile(dto.getEmail(), dto.getMobile())) {
				int otp = new SecureRandom().nextInt(1000, 10000);
				messageSendingHelper.sendOtp(dto.getName(), dto.getEmail(), otp);
				redisService.saveUserDto(dto);
				redisService.saveUserOtp(dto.getEmail(), otp);
				return ResponseEntity.status(201).body(new ResponseDto("Otp Sent Success, Verify to Continue", dto));
			} else {
				throw new DataExistsException(
						"Account Already Exists with " + dto.getEmail() + " or " + dto.getMobile());
			}
		} else {
			throw new DataExistsException(dto.getEmail() + " is Already being Verified if fails try after 15 mins");
		}
	}

	@Override
	public ResponseEntity<ResponseDto> verifyOtp(OtpDto dto) {
		int otp = redisService.fetchOtp(dto.getEmail());
		if (otp == 0)
			throw new ExpiredException("Otp Expired");
		else {
			if (otp == dto.getOtp()) {
				UserDto userDto = redisService.fetchUserDto(dto.getEmail());
				User user = userMapper.toEntity(userDto);
				userRepository.save(user);
				redisService.deleteUserDto(dto.getEmail());
				redisService.deleteUserOtp(dto.getEmail());
				return ResponseEntity.status(201).body(new ResponseDto("Account Created Success", userDto));
			} else {
				throw new MissMatchException("Otp Missmatch");
			}
		}
	}

	@Override
	public ResponseEntity<ResponseDto> resendOtp(String email) {
		if (redisService.fetchOtp(email) == 0)
			throw new DataNotFoundException(email + " doesnt exist");
		else {
			int otp = new SecureRandom().nextInt(1000, 10000);
			messageSendingHelper.sendOtp(redisService.fetchUserDto(email).getName(), email, otp);
			redisService.saveUserOtp(email, otp);
			return ResponseEntity.status(200)
					.body(new ResponseDto("Otp Re-Sent Success, Verify to Continue", redisService.fetchUserDto(email)));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> forgotPassword(String email) {
		if (!userRepository.existsByEmail(email))
			throw new DataNotFoundException("Invalid Email " + email);
		else {
			int otp = new SecureRandom().nextInt(1000, 10000);
			messageSendingHelper.sendForgotPasswordOtp(email, otp);
			redisService.saveUserOtp(email, otp);
			return ResponseEntity.status(200)
					.body(new ResponseDto("Otp for Reseting Password has been sent to " + email, email));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> resetPassword(ResetPasswordDto dto) {
		int otp = redisService.fetchOtp(dto.getEmail());
		if (otp == 0)
			throw new ExpiredException("Otp Expired Try Again");
		else {
			if (otp != dto.getOtp())
				throw new MissMatchException("Invalid Otp , Try Again");
			else {
				if (!userRepository.existsByEmail(dto.getEmail()))
					throw new DataNotFoundException("Account with " + dto.getEmail() + " doesnt exist, Try Again");
				else {
					User user = userRepository.findByEmail(dto.getEmail());
					user.setPassword(passwordEncoder.encode(dto.getPassword()));
					userRepository.save(user);

					redisService.deleteUserOtp(dto.getEmail());
					return ResponseEntity.status(200)
							.body(new ResponseDto("Password Reset Success", userMapper.toDto(user)));
				}
			}
		}
	}

	@Override
	public ResponseEntity<ResponseDto> login(LoginDto dto) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
		String token = jwtUtil.generateToken(userDetails);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("token", token);
		map.put("user", userMapper.toDto(userRepository.findByEmail(dto.getEmail())));
		return ResponseEntity.ok(new ResponseDto("Login Success", map));
	}

	@Override
	public ResponseEntity<ResponseDto> viewSavingsAccount(Principal principal) {
		User user = getLoggedInUser(principal);
		SavingBankAccount bankAccount = user.getBankAccount();
		if (bankAccount == null || !bankAccount.isActive())
			throw new DataNotFoundException("No Bank Account Exists for " + user.getName());
		else {
			return ResponseEntity.ok(new ResponseDto("Account Found", bankAccount));
		}
	}

	@Override
	public ResponseEntity<ResponseDto> createSavingsAccount(Principal principal, SavingAccountDto accountDto) {
		User user = getLoggedInUser(principal);
		if (user.getBankAccount() != null) {
			if (user.getBankAccount().isActive())
				throw new DataExistsException("Account Already Exists and You can not new Create One");
			else
				throw new DataExistsException("Account Still Pending for Verification Wait for some time");

		} else {
			SavingBankAccount bankAccount = bankMapper.toEntity(accountDto);
			savingAccountRepository.save(bankAccount);
			user.setBankAccount(bankAccount);
			userRepository.save(user);

			return ResponseEntity.status(201).body(new ResponseDto("Account Created Success", bankAccount));
		}
	}

	private User getLoggedInUser(Principal principal) {
		String email = principal.getName();
		User user = userRepository.findByEmail(email);
		if (user == null)
			throw new DataNotFoundException("Email Not Found in Database");
		else
			return user;
	}

}
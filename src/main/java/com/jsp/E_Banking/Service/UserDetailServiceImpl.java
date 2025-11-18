package com.jsp.E_Banking.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jsp.E_Banking.Entity.User;
import com.jsp.E_Banking.Exception.DataNotFoundException;
import com.jsp.E_Banking.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new DataNotFoundException("Invalid Email");
		} else {
			return new UserDetailsImpl(user);
		}
	}

}

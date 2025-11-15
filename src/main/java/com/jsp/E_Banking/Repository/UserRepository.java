package com.jsp.E_Banking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.E_Banking.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmailOrMobile(String email, String mobile);

	boolean existsByEmail(String email);

	User findByEmail(String email);

}
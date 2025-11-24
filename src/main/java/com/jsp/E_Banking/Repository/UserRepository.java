package com.jsp.E_Banking.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.E_Banking.Entity.User;
import com.jsp.E_Banking.dto.BankingRole;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmailOrMobile(String email, String mobile);

	boolean existsByEmail(String email);

	User findByEmail(String email);

	Optional<User> findByBankAccount_accountNumber(Long accountNumber);

	List<User> findByRole(BankingRole user);

}
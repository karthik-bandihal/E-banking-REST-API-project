package com.jsp.E_Banking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.E_Banking.Entity.SavingBankAccount;

public interface SavingAccountRepository extends JpaRepository<SavingBankAccount, Long> {

	List<SavingBankAccount> findByActiveFalse();

	
	

}

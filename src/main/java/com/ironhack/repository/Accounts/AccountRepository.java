package com.ironhack.repository.Accounts;

import com.ironhack.model.Accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAll();
    Account findAccountById(Long id);

}
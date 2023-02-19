package com.ironhack.repository.Users;

import com.ironhack.model.Users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHoldersRepository extends JpaRepository<AccountHolder, Long> {

    AccountHolder findAccountHolderById(Long id);
    AccountHolder findByName(String name);


}
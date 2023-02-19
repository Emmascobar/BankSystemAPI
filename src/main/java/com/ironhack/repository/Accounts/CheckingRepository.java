package com.ironhack.repository.Accounts;

import com.ironhack.model.Accounts.Checking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckingRepository extends JpaRepository<Checking, Long> {
    Checking findCheckingAccountById(Long id);

}
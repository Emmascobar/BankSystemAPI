package com.ironhack.repository.Accounts;

import com.ironhack.model.Accounts.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {

    Saving findSavingById(Long id);

}
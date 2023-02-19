package com.ironhack.repository.Utils;

import com.ironhack.model.Utils.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenceRepository extends JpaRepository<Transfer, Long> {
    Transfer findTransferenceById(Long id);
}
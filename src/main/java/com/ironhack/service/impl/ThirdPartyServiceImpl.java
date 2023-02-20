package com.ironhack.service.impl;

import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Users.ThirdPartyRepository;
import com.ironhack.repository.Utils.TransferRepository;
import com.ironhack.service.interfaces.ThirdPartyService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private TransferRepository transferRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public Transfer transfer(HttpServletRequest header, Transfer transfer) {
        /* Setting origin and destination account */
        Account destination = accountRepository.findById(transfer.getDestinationId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination ID not found"));;
        ThirdParty thirdPartyAccount = thirdPartyRepository.findById(transfer.getOwnerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Origin ID not found"));;
        String hashKey = header.getHeader("hashKey");

        // Checking Hashkey & secretKey // Normaly with ThrirdParty We no need check the balance because they should always have a positive balance.
        if (!thirdPartyAccount.getHashKey().equals(hashKey)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Incorrect hashKey");
        } else if(destination.getSecretKey().equals(transfer.getSecretKey())){
            destination.setBalance(new Money(destination.getBalance().getAmount().add(transfer.getAmount())));
            accountRepository.save(destination);
            thirdPartyAccount.setBalance(new Money(thirdPartyAccount.getBalance().getAmount().subtract(transfer.getAmount())));
            thirdPartyRepository.save(thirdPartyAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Wrong SecretKey");
        }
        /* Save & Registry transfer on system and database */
        log.info("Transfer registry on DateBase, from " + transfer.getId() + " to " + transfer.getDestinationId() + " with a amount of " + transfer.getAmount() + " at " + transfer.getDate());
        return transferRepository.save(transfer);
    }

}

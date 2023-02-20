package com.ironhack.controller.interfaces;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;
import org.springframework.security.core.Authentication;

public interface AccountHolderController {

    AccountHolder createAccount(Authentication authentication, AccountHolder accountHolder);
    AccountHolder getAccount(Authentication authentication, Long id);
    Money getCreditBalance (Authentication authentication, Long id);
    Money getSavingBalance(Authentication authentication, Long id);
    Transfer transfer(Authentication authentication, Transfer transfer);

}

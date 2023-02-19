package com.ironhack.service.interfaces;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;

public interface AccountHolderService {

    AccountHolder createAccount(AccountHolder accountHolder);
    AccountHolder getAccount(Long id);
    Money getCreditBalance (Long id);
    Money getSavingBalance(Long id);
    Transfer transfer(Transfer transfer);
}

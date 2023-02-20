package com.ironhack.controller.impl;

import com.ironhack.controller.interfaces.AccountHolderController;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.service.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountHolderControllerImpl implements AccountHolderController {
    @Autowired
    AccountHolderService accountHolderService;

    /* POSTMAPPING Create others account Holders */
    @GetMapping("user/accounts/account-holder")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder createAccount(Authentication authentication, @RequestBody AccountHolder accountHolder) {
        return accountHolderService.createAccount(accountHolder);
    }

    /* GETMAPPING Get Account Info- Account Holders */
    @GetMapping("user/accounts/account-holder/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder getAccount(Authentication authentication, @PathVariable Long id) {
        return accountHolderService.getAccount(id);
    }

    /* GETMAPPING Get Credit Balance - Account Holders */
    @GetMapping("user/login/accounts/{id}/credit-balance")
    @ResponseStatus(HttpStatus.OK)
    public Money getCreditBalance(Authentication authentication, @PathVariable Long id) {
        return accountHolderService.getCreditBalance(id);
    }
    /* GETMAPPING Get Saving Balance - Account Holders */
    @GetMapping("user/login/accounts/{id}/saving-balance")
    @ResponseStatus(HttpStatus.OK)
    public Money getSavingBalance(Authentication authentication, @PathVariable Long id) {
        return accountHolderService.getSavingBalance(id);
    }
    /* Transfers POSTMAPPING Account Holders */
    @PostMapping("User/login/accounts/transference")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer transfer(Authentication authentication, @RequestBody Transfer transfer) {
        return accountHolderService.transfer(transfer);
    }

}

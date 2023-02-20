package com.ironhack.controller.impl;

import com.ironhack.controller.DTOs.AccountBalanceDTO;
import com.ironhack.controller.DTOs.CreditLimitDTO;
import com.ironhack.controller.DTOs.InterestRateDTO;
import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.controller.interfaces.AdminController;
import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Accounts.Checking;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Users.User;
import com.ironhack.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AdminControllerImpl implements AdminController {

    @Autowired
    AdminService adminService;

    /* GETMAPPING routes */
    @GetMapping("/admin/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAllAccounts() {
        return adminService.getAllAccounts();
    }
    @GetMapping("/admin/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountById(@PathVariable Long id) {
        return adminService.getAccountById(id);
    }
    @GetMapping("/admin/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    /** POSTMAPPING routes **/
    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin addNewAdminUser(@RequestBody @Valid Admin admin) {
        return adminService.addNewAdminUser(admin);
    }
    @PostMapping("/admin/users/account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder addNewAccountHolder(@RequestBody @Valid AccountHolder accountHolder) {
        return adminService.addNewAccountHolder(accountHolder); }
    @PostMapping("/admin/users/third-party")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty addNewThirdPartyAccount(@RequestBody @Valid ThirdParty thirdParty) {
        return adminService.addNewThirdPartyUser(thirdParty);}
    @PostMapping("/admin/accounts/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Account addNewSavingAccount(@RequestBody @Valid Saving saving) {
        return adminService.addNewSavingAccount(saving);   }
    @PostMapping("/admin/accounts/checking")
    @ResponseStatus(HttpStatus.CREATED)
    public Account addNewCheckingAccount(@RequestBody @Valid Checking checking) {
        return adminService.addNewCheckingAccount(checking);    }
    @PostMapping("/admin/accounts/credit-cards")
    @ResponseStatus(HttpStatus.CREATED)
    public Account addNewCreditCard(@RequestBody @Valid CreditCard creditCard) {
        return adminService.addNewCreditCard(creditCard);    }

    /** PUTMAPPING routes **/
    @PutMapping("/admin/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSavingAccount(@PathVariable Long id, @RequestBody @Valid Saving saving) {
        adminService.updateSavingAccount(id, saving);    }

    /** PATCHMAPPING ROUTES **/
    @PatchMapping("/admin/users/balance")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@RequestBody @Valid AccountBalanceDTO accountBalanceDTO) {
        adminService.updateBalance(accountBalanceDTO);    }
    @PatchMapping("/admin/credit-cards/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCreditLimit(@PathVariable Long id, @RequestBody @Valid CreditLimitDTO creditLimitDTO) {
        adminService.updateCreditLimit(id, creditLimitDTO);    }
    @PatchMapping("/admin/accounts/savings/interest-rate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSavingInterestRate(@PathVariable Long id, @RequestBody @Valid InterestRateDTO interestRateDTO) {
        adminService.updateSavingInterestRate(id, interestRateDTO);    }
    @PatchMapping("/admin/accounts/credit-cards/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCreditInterestRate(Long id, @RequestBody @Valid InterestRateDTO interestRateDTO) {
        adminService.updateCreditInterestRate(id, interestRateDTO);    }
    @PatchMapping("/admin/accounts/savings/minimum-balance/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMinimumBalance(@PathVariable Long id, @RequestBody @Valid SavingMinimumBalanceDTO savingMinimumBalanceDTO) {
        adminService.updateMinimumBalance(id, savingMinimumBalanceDTO);    }

    /** DELETE-MAPPING routes* */
    @DeleteMapping("/admin/accounts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id) {
        adminService.deleteAccount(id);
    }

}

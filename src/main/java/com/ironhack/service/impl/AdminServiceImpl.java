package com.ironhack.service.impl;

import com.ironhack.controller.DTOs.AccountBalanceDTO;
import com.ironhack.controller.DTOs.CreditLimitDTO;
import com.ironhack.controller.DTOs.InterestRateDTO;
import com.ironhack.controller.DTOs.SavingMinimumBalanceDTO;
import com.ironhack.model.Accounts.*;
import com.ironhack.model.Users.*;
import com.ironhack.model.Utils.Money;
import com.ironhack.repository.Accounts.*;
import com.ironhack.repository.Users.*;
import com.ironhack.repository.Utils.TransferRepository;
import com.ironhack.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    // Autowired
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    StudentCheckingRepository studentCheckingRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    SavingRepository savingRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AccountHoldersRepository accountHoldersRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    String encodedPassword;
    Role role;
    @Autowired
    private UserRepository userRepository;


    /** ------------------------------------------------------------------------------------ **/

    /* Find and get all accounts */
    public List<Account> getAllAccounts() { return accountRepository.findAll();}
    /* Find and get an account by ID */
    public Account getAccountById(Long id) {return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));}
    /* Find and get all Users */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    /* Create a new admin User account */
    public Admin addNewAdminUser(Admin admin) {
        encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        role = new Role("ADMIN");
        adminRepository.save(admin);
        return admin;
    }
    /* Create a new AccountHolder User */
    public AccountHolder addNewAccountHolder(AccountHolder accountHolder) {
        encodedPassword = passwordEncoder.encode(accountHolder.getPassword());
        accountHolder.setPassword(encodedPassword);
        role = new Role("ACCOUNT_HOLDER");
        accountHoldersRepository.save(accountHolder);
        return accountHolder;
    }
    /* Create a new TPUSer */
    public ThirdParty addNewThirdPartyUser(ThirdParty thirdParty) {
     /* Should active this lines if you want to protect the hashKey*/
    //        encodedPassword = passwordEncoder.encode(thirdParty.getHashKey());
    //        thirdParty.setHashKey(encodedPassword);
    //        role = new Role("NONE");
        thirdPartyRepository.save(thirdParty);
        return thirdParty;
    }

    /* Add a new Saving Bank Account */
    public Saving addNewSavingAccount(Saving saving) {
        savingRepository.save(saving);
        return saving;
    }

    /* Add a new Checking Bank Account / Age condition (>24) to create a student or normal account */
    public Checking addNewCheckingAccount(Checking checking) {
        /* Get the age from the birthday with ChronoUnit */
        LocalDate today = LocalDate.now();
        LocalDate birthday = checking.getPrimaryOwner().getDateOfBirth();
        Long age = ChronoUnit.YEARS.between(birthday, today);
        /* Then created conditions for new accounts by Age */
        if (age < 24) {
            // STUDENT ACCOUNT CREATED
            StudentChecking studentChecking = new StudentChecking();
            // CAST PARAMS FROM CHECKING TO STUDENT
            studentChecking.setPrimaryOwner(checking.getPrimaryOwner());
            studentChecking.setSecondaryOwner(checking.getSecondaryOwner());
            studentChecking.setSecretKey(checking.getSecretKey());
            // SAVE STUDENT CHECKING IN REPOSITORY
            studentCheckingRepository.save(studentChecking);
        } else {
            // A NORMAL CHECKING ACCOUNT CREATED
        }
        return checkingRepository.save(checking);
    }

    /* Add a new CreditCard */
    public CreditCard addNewCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }
    /* Update Balance */
    public void updateBalance(AccountBalanceDTO accountBalanceDTO){
        Optional<Account> optionalAccount = accountRepository.findById(accountBalanceDTO.getAccountId());
        if (!optionalAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
            optionalAccount.get().setBalance(new Money(accountBalanceDTO.getNewBalance()));
            accountRepository.save(optionalAccount.get());
    }
    /* Update a Saving account */
    public void updateSavingAccount(Long id, Saving saving) {
        Optional<Saving> optionalAccount = savingRepository.findById(id);
        if (optionalAccount.isPresent()) {
            saving.setId(id);
            accountRepository.save(saving);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
    }
    /* Update Credits limits */
    public void updateCreditLimit(Long id, CreditLimitDTO creditLimitDTO) {
        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit Card ID not found"));
        /* Check conditions for limits (>100  && < 100000) */
        BigDecimal creditLimit = creditLimitDTO.getCreditLimit();
        if (creditLimit.compareTo(new BigDecimal(100)) == -1 || creditLimit.compareTo(new BigDecimal(100000)) == 1) {
            throw new RuntimeException("Credit Limit cannot be lower than 100 or higher than 100000. Define a new one");
        }
        creditCard.setCreditLimit(creditLimit);
        creditCardRepository.save(creditCard);
    }
    // Update accounts interest rate // SAVINGS AND CREDIT CARDS.
    public void updateSavingInterestRate(Long id, InterestRateDTO interestRateDTO) {
        Saving saving = savingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saving count not found"));
        BigDecimal interestRate = interestRateDTO.getInterestRate();
        /* Update Saving Interest rate */
            if (interestRate.compareTo(new BigDecimal("0.5")) == 1 || interestRate.compareTo(new BigDecimal("0.0025")) == -1) {
                throw new RuntimeException("Interest Rate cannot be higher than 0.5 OR lower than 0.0025. Define a new one");
            } else {
                saving.setInterestRate(interestRate);
                savingRepository.save(saving);
            }
        }
        /* Update Credit Card Interest rate */
        public void updateCreditInterestRate(Long id, InterestRateDTO interestRateDTO) {
            CreditCard creditCard = creditCardRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saving count not found"));
            BigDecimal interestRate = interestRateDTO.getInterestRate();
            if (interestRate.compareTo(new BigDecimal("0.1")) == -1 || interestRate.compareTo(new BigDecimal("0.2")) == 1) {
                throw new RuntimeException("Interest Rate cannot be lower than 0.1 or higher than 0.2. Default value = 0.2. Define a new one");
            } else {
                creditCard.setInterestRate(interestRate);
                creditCardRepository.save(creditCard);
            }
        }

    // Update Minimum Balance // SAVING ACCOUNTS.
    public void updateMinimumBalance(Long id, SavingMinimumBalanceDTO savingMinimumBalanceDTO) {
        Saving updateSavingBalance = savingRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Saving account not found"));
        BigDecimal minimumBalance = savingMinimumBalanceDTO.getMinimumBalance().getAmount();
        updateSavingBalance.setMinimumBalance(new Money(minimumBalance));
            savingRepository.save(updateSavingBalance);
        }
    // Delete a account
    public void deleteAccount(Long id) {
        if (!accountRepository.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        accountRepository.deleteById(id);
    }
}
package com.ironhack.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.model.Accounts.Checking;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Address;
import com.ironhack.model.Utils.Money;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Accounts.SavingRepository;
import com.ironhack.repository.Users.AdminRepository;
import com.ironhack.repository.Users.ThirdPartyRepository;
import com.ironhack.repository.Users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
class SavingsRepositoryTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    UserRepository userRepository;
    private CreditCard creditCard;
    private AccountHolder accountHolder1, accountHolder2, accountHolder3;
    private Checking checking;
    private Saving saving01, saving02, saving03;
    private ThirdParty thirdParty;
    private Address address01, address02, address03;
    private Admin admin;
    @Autowired
    private SavingRepository savingRepository;


    @BeforeEach
    void setUp() {
        /* ADDRESS */
        address01 = new Address("Carrer de la Argentina 3", "Barcelona", 8041);
        address02 = new Address("Calle Ecuador", "Buenos Aires", 2041);
        address03 = new Address("Lincoln Road Avenue 963", "Miami", 1963);
        /* SET USERS - ACCOUNTS-HOLDERS */
        admin = new Admin("Pavlo Menendez", "pavlomenendez88", passwordEncoder.encode("pavlomenendez88"));
        adminRepository.save(admin);
        accountHolder1 = new AccountHolder("Matias Fabbro", "mattfabbro", passwordEncoder.encode("mattfabbro88"), LocalDate.of(1988, 03, 24), address01, address03);
        accountHolder2 = new AccountHolder("Pavlo Menendez", "pavlomenendez", passwordEncoder.encode("pavlomenendez88"), LocalDate.of(1988, 06, 14), address02, null);
        accountHolder3 = new AccountHolder("Jeremias Fabbro", "jerefabbro", passwordEncoder.encode("jerefabbro98"), LocalDate.of(1998, 02, 21), address03, null);
        userRepository.saveAll(List.of(accountHolder1, accountHolder2, accountHolder3));
        /* BANK ACCOUNTS */
        saving01 = new Saving(123456, accountHolder1, null);
        saving02 = new Saving(456789, accountHolder2, null);
        saving03 = new Saving(568942, accountHolder3, accountHolder1);
        accountRepository.saveAll(List.of(saving01, saving02, saving03));
        checking = new Checking(568942, accountHolder3, null);
        accountRepository.save(checking);
        creditCard = new CreditCard(456789, accountHolder2, null);
        accountRepository.save(creditCard);
        thirdParty = new ThirdParty(new Money(new BigDecimal("500")), "AAB123456");
        thirdPartyRepository.save(thirdParty);
    }

    @AfterEach
    void tearDown() {
        thirdPartyRepository.deleteAll();
    }

    @Test
    void jpaMethods_TheyWorkAsExpected(){
        Optional<Saving> optionalSaving = savingRepository.findById(saving01.getId());
        assertTrue(optionalSaving.isPresent());
        assertEquals(saving01.getPrimaryOwner().getName(), optionalSaving.get().getPrimaryOwner().getName());
    }
}
package com.ironhack.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.controller.DTOs.AccountBalanceDTO;
import com.ironhack.model.Accounts.Checking;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Admin;
import com.ironhack.model.Users.Role;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Utils.Address;
import com.ironhack.model.Utils.Money;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Accounts.CheckingRepository;
import com.ironhack.repository.Accounts.CreditCardRepository;
import com.ironhack.repository.Accounts.SavingRepository;
import com.ironhack.repository.Users.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired

    SavingRepository savingRepository;
    @Autowired

    AccountRepository accountRepository;
    @Autowired

    CreditCardRepository creditCardRepository;
    @Autowired
     AccountHoldersRepository accountHoldersRepository;
    @Autowired
    CheckingRepository checkingRepository;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    CreditCard creditCard;
    private AccountHolder accountHolder1, accountHolder2, accountHolder3;
    private Checking checking;
    private Saving saving01, saving02, saving03;
    private ThirdParty thirdParty;
    private Role adminUserRole, holderUserRole;
    private Address address01, address02, address03;
    private Admin admin;


    @BeforeEach
    void setUp() {
    /* ADDRESS */
    address01 = new Address("Carrer de la Argentina 3", "Barcelona", 8041);
    address02 = new Address("Calle Ecuador", "Buenos Aires", 2041);
    address03 = new Address("Lincoln Road Avenue 963", "Miami", 1963);
    /* SET USERS - ACCOUNTS-HOLDERS */
    admin = new Admin("Pavlo Menendez", "pavlomenendez88", passwordEncoder.encode("pavlomenendez88"));
    adminRepository.save(admin);
    accountHolder1 = new AccountHolder("Matias Fabbro", "mattfabbro", passwordEncoder.encode("mattfabbro88"),  LocalDate.of(1988,03,24), address01, address03);
    accountHolder2 = new AccountHolder("Pavlo Menendez", "pavlomenendez", passwordEncoder.encode("pavlomenendez88"),LocalDate.of(1988,06,14), address02, null);
    accountHolder3 = new AccountHolder("Jeremias Fabbro", "jerefabbro", passwordEncoder.encode("jerefabbro98"), LocalDate.of(1998,02,21), address03, null);
    userRepository.saveAll(List.of(accountHolder1, accountHolder2, accountHolder3));
    /* BANK ACCOUNTS */
    saving01 = new Saving( 123456, accountHolder1, null);
    saving02 = new Saving( 456789, accountHolder2, null);
    saving03 = new Saving( 568942, accountHolder3, accountHolder1);
    accountRepository.saveAll(List.of(saving01, saving02, saving03));
    checking = new Checking( 568942, accountHolder3, null);
    accountRepository.save(checking);
    creditCard = new CreditCard(456789, accountHolder2, null );
    accountRepository.save(creditCard);
    thirdParty = new ThirdParty(new Money(new BigDecimal("500")), "AAB123456");
    thirdPartyRepository.save(thirdParty);
}
    @AfterEach
    void tearDown() {
        adminRepository.deleteAll();
    }

    @Test
    void getAll_NoParams_Accounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/admin/accounts"))
                .andExpect(status().isOk()) // RESPONSE HTTP 200 - OK //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pavlo Menendez"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }

    @Test
    void getAccountById_ValidId_GetAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/admin/accounts/"+ saving01.getId()))
                .andExpect(status().isOk()) // RESPONSE HTTP 200 - OK //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Matias Fabbro"));
    }

    @Test
    void getAll_Users_GetUser() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/admin/users"))
                .andExpect(status().isOk()) // RESPONSE HTTP 200 - OK //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pavlo Menendez"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }

    @Test
    void save_ValidUser_NewAdminUser() throws Exception {
        String body = objectMapper.writeValueAsString(admin);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pavlo Menendez"));
    }

    @Test
    void store_ValidAccountHolder_AddNewAccountHolder() throws Exception {
        String body = objectMapper.writeValueAsString(accountHolder3);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/users/account-holder")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }

    @Test
    void store_AddNewThirdParty_AddNewAccount() throws Exception {
        String body = objectMapper.writeValueAsString(thirdParty);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/users/third-party")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("AAB123456"));
    }


    @Test
    void store_ValidSavingAccount_addNew() throws Exception {
        String body = objectMapper.writeValueAsString(saving03);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/accounts/savings")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }
    @Test
    void store_CheckingAccount_addNew() throws Exception {
        String body = objectMapper.writeValueAsString(checking);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/accounts/checking")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }
    @Test
    void store_CreditCard_addNew() throws Exception {
        String body = objectMapper.writeValueAsString(creditCard);
        MvcResult mvcResult = mockMvc.perform(
                        post("/admin/accounts/credit-cards")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated()) // RESPONSE HTTP 201 - CREATED //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pavlo Menendez"));
    }

    @Test
    void change_updateValidAccount_NoReturn() throws Exception {
        String body = objectMapper.writeValueAsString(saving03);
        MvcResult mvcResult = mockMvc.perform(
                        put("/admin/accounts/"+saving03.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // 204
                .andReturn();

        Optional<Saving> optionalSaving = savingRepository.findById(saving03.getId());
        assertTrue(optionalSaving.isPresent());
        assertEquals("Jeremias Fabbro", optionalSaving.get().getPrimaryOwner().getName());
    }

    @Test
    void change_updateBalance_NoReturn() throws Exception {
        AccountBalanceDTO balanceDTO = new AccountBalanceDTO(saving01.getId(), new BigDecimal("1000"));
        String body = objectMapper.writeValueAsString(balanceDTO);
        MvcResult mvcResult = mockMvc.perform(
                        patch("/admin/users/balance")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
                .andReturn();
        Optional<Saving> optionalSaving = savingRepository.findById(saving01.getId());
        assertTrue(optionalSaving.isPresent());
        assertEquals(new BigDecimal("1000"), balanceDTO.getNewBalance());
    }

    @Test
    void update_CreditLimit_noReturn() throws Exception {
        creditCard.setCreditLimit(new BigDecimal("300"));
        String body = objectMapper.writeValueAsString(creditCard);
        MvcResult mvcResult = mockMvc.perform(
                        patch("/admin/credit-cards/" + creditCard.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
                .andReturn();

        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(creditCard.getId());
        assertTrue(optionalCreditCard.isPresent());
        /* Run with valid params (Credit Limits < 1000) */
        assertEquals(new BigDecimal("300"), creditCard.getCreditLimit());
    }

    @Test
    void change_SavingInterestRate_NoReturn() throws Exception {
        saving01.setInterestRate(new BigDecimal("0.15"));
        String body = objectMapper.writeValueAsString(saving01);
        MvcResult mvcResult = mockMvc.perform(
                        patch("/admin/accounts/savings/interest-rate/" + saving01.getId())
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent()) // RESPONSE HTTP 204 - NO CONTENTS //
                .andReturn();

        Optional<Saving> optionalSaving = savingRepository.findById(saving01.getId());
        assertTrue(optionalSaving.isPresent());
        assertEquals(new BigDecimal("0.15"), optionalSaving.get().getInterestRate());
    }

    @Test
    void delete_ValidAccount_CourseRemoved() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/admin/accounts/"+ saving02.getId()))
                .andExpect(status().isNoContent())
                .andReturn();
        assertFalse(savingRepository.existsById(saving02.getId()));
    }
}
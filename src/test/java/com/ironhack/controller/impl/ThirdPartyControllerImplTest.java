package com.ironhack.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ironhack.controller.interfaces.AdminController;
import com.ironhack.model.Accounts.Account;
import com.ironhack.model.Accounts.Checking;
import com.ironhack.model.Accounts.CreditCard;
import com.ironhack.model.Accounts.Saving;
import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Users.Role;
import com.ironhack.model.Users.ThirdParty;
import com.ironhack.model.Users.User;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.Utils.Transfer;
import com.ironhack.repository.Accounts.AccountRepository;
import com.ironhack.repository.Users.RoleRepository;
import com.ironhack.repository.Users.ThirdPartyRepository;
import com.ironhack.repository.Utils.TransferenceRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class ThirdPartyControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    TransferenceRepository transferenceRepository;
    @Autowired
    RoleRepository roleRepository;
    private Role adminUserRole, holderUserRole;
    private CreditCard creditCard;
    private AdminController adminController;
    private AccountHolder accountHolder1, accountHolder2, accountHolder3;
    Transfer transfer;
    ThirdParty thirdParty;
    User user;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    @Autowired
    AccountRepository accountRepository;
    private Account account;
    private Checking checking;
    private Saving saving01, saving02, saving03;

    @BeforeEach
    void setUp() {
        /* SET USERS AND TIPES OF ROLES */
        thirdParty = new ThirdParty(new Money(new BigDecimal("500")), "AAB123456");
        thirdPartyRepository.save(thirdParty);
        transfer = new Transfer(new BigDecimal("100"), 2L, "Emma", 1L, 456789);
        transferenceRepository.save(transfer);
        /* BANK ACCOUNTS */
        saving01 = new Saving(123456, accountHolder1, null);
        saving02 = new Saving(456789, accountHolder2, null);
        saving03 = new Saving(568942, accountHolder3, accountHolder1);
        accountRepository.saveAll(List.of(saving01, saving02, saving03));
        checking = new Checking(568942, accountHolder3, null);
        accountRepository.save(checking);
        creditCard = new CreditCard(456789, accountHolder2, null);
        accountRepository.save(creditCard);
    }

    @AfterEach
    void tearDown() {
        thirdPartyRepository.deleteAll();
    }

    @Test
    void make_transfer_ChangeBalance() throws Exception {
        saving01.setBalance(new Money(new BigDecimal("1000")));
        accountRepository.save(saving01);
        String body = objectMapper.writeValueAsString(transfer);

        MvcResult mvcResult = mockMvc.perform(
                        post("/User/accounts/thirdparty/transfer/").header("hashKey", "AAB123456")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        // testing
        assertEquals(1, transferenceRepository.findAll().size());
        assertEquals(new BigDecimal("1000"), saving01.getBalance().getAmount());
    }
    @Test
    void make_transfer_ChangeBalance2() throws Exception {
        saving01.setBalance(new Money(new BigDecimal("1000")));
        accountRepository.save(saving01);
        String body = objectMapper.writeValueAsString(transfer);

        MvcResult mvcResult = mockMvc.perform(
                        post("/User/accounts/thirdparty/transfer/").header("hashKey", "AAB123456")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        // Parse response to JSON
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());

        // Get hashKey
        String token = jsonObject.getString("hashKey");
        System.out.println(token);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "hashKey " + "AAB123456");
        System.out.println(httpHeaders);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/login/accounts/" + accountHolder2.getId()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Matias Fabbro"));
        // testing
        assertEquals(1, transferenceRepository.findAll().size());
        assertEquals(new BigDecimal("1000"), saving01.getBalance().getAmount());

    }
}
package com.ironhack.controller.impl;

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
import com.ironhack.repository.Users.AdminRepository;
import com.ironhack.repository.Users.ThirdPartyRepository;
import com.ironhack.repository.Users.UserRepository;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountHolderControllerImplTest {

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
        accountRepository.deleteAll();
    }

    @Test
    void OwnAccount_ValidId_GetInformation() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/user/accounts/account-holder/" + accountHolder3.getId()))
                .andExpect(status().isOk()) // RESPONSE HTTP 200 - OK //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jeremias Fabbro"));
    }
    @Test
        void creditBalance_LoggedUser_Result () throws Exception {
            MvcResult mvcResult = mockMvc.perform(

                            MockMvcRequestBuilders.get("/login")
                                    .param("username", "mattfabbro")
                                    .param("password", "mattfabbro88")
                    )
                    .andExpect(status().isOk())
                    .andReturn();
            // Parse response to JSON
            JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());

            // Get access token
            String token = jsonObject.getString("access_token");

            System.out.println(token);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token);
            System.out.println(httpHeaders);

            mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/login/accounts/" + accountHolder1.getId() + "credit-balance").headers(httpHeaders))
                    .andExpect(status().isOk())
                    .andReturn();
            assertTrue(mvcResult.getResponse().getContentAsString().contains("Matias Fabbro"));
        }
    @Test
    void savingBalance_LoggedUser_Result () throws Exception {
        MvcResult mvcResult = mockMvc.perform(

                        MockMvcRequestBuilders.get("/login")
                                .param("username", "pavlomenendez")
                                .param("password", "pavlomenendez88")
                )
                .andExpect(status().isOk())
                .andReturn();
        // Parse response to JSON
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());

        // Get access token
        String token = jsonObject.getString("access_token");
        System.out.println(token);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        System.out.println(httpHeaders);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/login/accounts/" + accountHolder2.getId() + "saving-balance").headers(httpHeaders))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Pavlo Menendez"));
    }
}
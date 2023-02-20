package com.ironhack.model.Users;

import com.ironhack.model.Utils.Money;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
public class ThirdParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @NotEmpty
    @DecimalMin(value = "0")
    private Money balance;
    @NotBlank(message = "Insert a hashKey")
    private String hashKey;
    public ThirdParty() {
    }
    public ThirdParty(Money balance, String hashKey) {
        this.balance = balance;
        this.hashKey = hashKey;
    }
    public Long getId() {
        return Id;
    }
    public void setId(Long id) {
        Id = id;
    }
    public Money getBalance() {
        return balance;
    }
    public void setBalance(Money balance) {
        this.balance = balance;
    }
    public String getHashKey() {
        return hashKey;
    }
    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }
}
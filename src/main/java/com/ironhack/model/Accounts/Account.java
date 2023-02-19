package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import com.ironhack.model.enums.Status;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public abstract class Account {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Money balance;
    @NotEmpty(message = "Secret key is necessary")
    private Integer secretKey;
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "primaryOwner")
    @NotEmpty(message = "Primary Owner is mandatory")
    private AccountHolder primaryOwner;
    @ManyToOne
    @JoinColumn(name = "secondaryOwner")
    @Nullable
    private AccountHolder secondaryOwner;
    private final LocalDate creationDate = LocalDate.now();
    @Enumerated(EnumType.STRING)
    private Status status;
    public Account() {
    }
    public Account(Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = new Money(new BigDecimal("0"), Currency.getInstance("USD"), RoundingMode.HALF_EVEN);
        this.secretKey = secretKey;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.status = Status.ACTIVE;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Money getBalance() {
        return balance;
    }
    public void setBalance(Money balance) {
        this.balance = balance;
    }
    public Integer getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(Integer secretKey) {
        this.secretKey = secretKey;
    }
    public AccountHolder getPrimaryOwner() {
        return primaryOwner;
    }
    public void setPrimaryOwner(AccountHolder primaryOwner) {
        this.primaryOwner = primaryOwner;
    }
    public AccountHolder getSecondaryOwner() {
        return secondaryOwner;
    }
    public void setSecondaryOwner(AccountHolder secondaryOwner) {
        this.secondaryOwner = secondaryOwner;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }


}
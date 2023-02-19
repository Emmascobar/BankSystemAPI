package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class CreditCard extends Account{
    @DecimalMin(value = "100")
    @DecimalMax(value = "100000")
    @NotEmpty(message = "Credit limit cannot be empty")
    private BigDecimal creditLimit;
    @DecimalMin(value = "0.1")
    @DecimalMax(value = "0.2")
    @NotEmpty(message = "Interest rate cannot be empty")
    private BigDecimal interestRate;
    private LocalDate lastUpdate;
    public CreditCard() {
    }
    public CreditCard(Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(secretKey, primaryOwner, secondaryOwner);
        this.creditLimit = new BigDecimal("100");
        this.interestRate = new BigDecimal("0.2");
        this.lastUpdate = LocalDate.now();
    }
    public CreditCard(Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal creditLimit, BigDecimal interestRate) {
        super(secretKey, primaryOwner, secondaryOwner);
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.lastUpdate = LocalDate.now();
    }
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public LocalDate getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public void setMonthlyInterestRate() {
        long elapsedTime = ChronoUnit.MONTHS.between(getLastUpdate(), LocalDate.now());
        if (elapsedTime > 1) {
            BigDecimal monthlyInterest = getInterestRate().divide(new BigDecimal("12"));
            setBalance(new Money(getBalance().getAmount().add(monthlyInterest)));
            setLastUpdate(LocalDate.now());
        }
    }
}
package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import jakarta.persistence.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Currency;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class Saving extends Account {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    @DecimalMin(value = "100" )
    @DecimalMax(value = "1000")
    private Money minimumBalance;
    @DecimalMax(value = "0.5")
    @DecimalMin(value = "0")
    private BigDecimal interestRate;
    @Nullable
    private BigDecimal penaltyFee;
    @DateTimeFormat(pattern = "dd MM yyyy")
    private LocalDate lastUpdate;
    public Saving() {
    }
    public Saving(Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super( secretKey, primaryOwner, secondaryOwner);
        this.lastUpdate = LocalDate.now();
        this.minimumBalance = new Money(new BigDecimal("1000"), Currency.getInstance("USD"), RoundingMode.HALF_EVEN);
        this.interestRate = new BigDecimal("0.0025");
        this.penaltyFee = new BigDecimal("40");
    }
    public Saving(Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner, Money minimumBalance, BigDecimal interestRate) {
        super(secretKey, primaryOwner, secondaryOwner);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.penaltyFee = new BigDecimal("40");
        this.lastUpdate = LocalDate.now();
    }
    public Money getMinimumBalance() {
        return minimumBalance;
    }
    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public LocalDate getLastUpdate() {
        return lastUpdate;
    }
    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }
    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }
    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    // ANNUAL INTEREST RATE METHOD // USED CHRONOUnit to set last updates times/ Apply in Saving Services.
    public void setAnnualInterestRate(){
        long elapsedTime = ChronoUnit.YEARS.between(getLastUpdate(), LocalDate.now());
        if (elapsedTime > 1) {
            BigDecimal annualRate = getBalance().getAmount().multiply(interestRate);
            setBalance(new Money(getBalance().getAmount().add(annualRate)));
            setLastUpdate(LocalDate.now());
        }
    }
    // PENALTY FEE METHOD // Apply in Savings Services.
    public void applyPenaltyFee() {
        if (getBalance().getAmount().compareTo(getMinimumBalance().getAmount())  == -1) {
            setBalance(new Money(getBalance().getAmount().subtract(penaltyFee)));
        }
    }
}

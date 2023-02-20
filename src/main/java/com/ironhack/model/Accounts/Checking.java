package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import com.ironhack.model.Utils.Money;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class Checking extends Account {

    @Embedded
    @NotEmpty(message = "Minimum balance cannot be empty")
    private Money minimumBalance;
    @NotEmpty(message = "monthly maintenance fee cannot be empty")
    private BigDecimal monthlyMaintenanceFee;
    @Nullable
    private BigDecimal penaltyFee;
    public Checking() {
    }
    public Checking(Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(secretKey, primaryOwner, secondaryOwner);
        this.minimumBalance = new Money(new BigDecimal("250"), Currency.getInstance("USD"), RoundingMode.HALF_EVEN);
        this.monthlyMaintenanceFee = new BigDecimal("12");
        this.penaltyFee = new BigDecimal("40");
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }
    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    public BigDecimal getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }
    public void setMonthlyMaintenanceFee(BigDecimal monthlyMaintenanceFee) {
        this.monthlyMaintenanceFee = monthlyMaintenanceFee;
    }
}



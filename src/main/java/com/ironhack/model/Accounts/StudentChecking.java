package com.ironhack.model.Accounts;

import com.ironhack.model.Users.AccountHolder;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class StudentChecking extends Account{
    @Nullable
    private BigDecimal penaltyFee;
    public StudentChecking() {
    }
    public StudentChecking( Integer secretKey, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super( secretKey, primaryOwner, secondaryOwner);
        this.penaltyFee = new BigDecimal("40");
    }
    public BigDecimal getPenaltyFee() {
        return penaltyFee;
    }
    public void setPenaltyFee(BigDecimal penaltyFee) {
        this.penaltyFee = penaltyFee;
    }
}

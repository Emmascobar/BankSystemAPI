package com.ironhack.controller.DTOs;

import java.math.BigDecimal;

public class AccountBalanceDTO {
    private Long accountId;
    private BigDecimal newBalance;

    public AccountBalanceDTO(Long accountId, BigDecimal newBalance) {
        this.accountId = accountId;
        this.newBalance = newBalance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }
}

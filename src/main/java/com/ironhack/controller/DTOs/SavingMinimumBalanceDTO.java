package com.ironhack.controller.DTOs;

import com.ironhack.model.Utils.Money;

public class SavingMinimumBalanceDTO {

    private Money minimumBalance;

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}

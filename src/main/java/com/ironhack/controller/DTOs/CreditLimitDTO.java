package com.ironhack.controller.DTOs;

import java.math.BigDecimal;

public class CreditLimitDTO {
    private BigDecimal creditLimit;

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
}

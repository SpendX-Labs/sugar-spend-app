package com.finance.sugarmarket.app.budgetview.dto;

import java.math.BigDecimal;

public class TimeBasedSummary {
    private String dataKey;
    private BigDecimal manualAmount;
    private BigDecimal cardAmount;

    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public BigDecimal getManualAmount() {
        return manualAmount;
    }

    public void setManualAmount(BigDecimal manualAmount) {
        this.manualAmount = manualAmount;
    }

    public BigDecimal getCardAmount() {
        return cardAmount;
    }

    public void setCreditCardAmount(BigDecimal cardAmount) {
        this.cardAmount = cardAmount;
    }

    public TimeBasedSummary(String dataKey, BigDecimal manualAmount, BigDecimal cardAmount) {
        super();
        this.dataKey = dataKey;
        this.manualAmount = manualAmount;
        this.cardAmount = cardAmount;
    }

    public TimeBasedSummary() {
        super();
    }
}

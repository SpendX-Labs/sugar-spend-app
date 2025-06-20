package com.finance.sugarmarket.app.dto;

import java.math.BigDecimal;

public class LoanPrepaymentRequestDto {
    private Long id;
    private Integer updatedRemainingTenure;
    private BigDecimal principalRepay;
    private Integer alreadyPaidMonth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUpdatedRemainingTenure() {
        return updatedRemainingTenure;
    }

    public void setUpdatedRemainingTenure(Integer updatedRemainingTenure) {
        this.updatedRemainingTenure = updatedRemainingTenure;
    }

    public BigDecimal getPrincipalRepay() {
        return principalRepay;
    }

    public void setPrincipalRepay(BigDecimal principalRepay) {
        this.principalRepay = principalRepay;
    }

    public Integer getAlreadyPaidMonth() {
        return alreadyPaidMonth;
    }

    public void setAlreadyPaidMonth(Integer alreadyPaidMonth) {
        this.alreadyPaidMonth = alreadyPaidMonth;
    }
}

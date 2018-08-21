package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;

public class DirectPrizeVo
{
    private Integer id;
    private Integer refereeId;
    private Integer coverRefereeId;
    private Integer coinId;
    private BigDecimal amountUsed;
    private BigDecimal amount;
    private BigDecimal amountAvailable;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getRefereeId()
    {
        return this.refereeId;
    }

    public void setRefereeId(Integer refereeId)
    {
        this.refereeId = refereeId;
    }

    public Integer getCoverRefereeId()
    {
        return this.coverRefereeId;
    }

    public void setCoverRefereeId(Integer coverRefereeId)
    {
        this.coverRefereeId = coverRefereeId;
    }

    public Integer getCoinId()
    {
        return this.coinId;
    }

    public void setCoinId(Integer coinId)
    {
        this.coinId = coinId;
    }

    public BigDecimal getAmountUsed()
    {
        return this.amountUsed;
    }

    public void setAmountUsed(BigDecimal amountUsed)
    {
        this.amountUsed = amountUsed;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getAmountAvailable()
    {
        return this.amountAvailable;
    }

    public void setAmountAvailable(BigDecimal amountAvailable)
    {
        this.amountAvailable = amountAvailable;
    }
}

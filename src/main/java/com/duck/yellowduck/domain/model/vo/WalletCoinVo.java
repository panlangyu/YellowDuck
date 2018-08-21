package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;

/**
 * VO
 */
public class WalletCoinVo
{
    private Integer id;
    private String address;
    private String contractAddr;
    private BigDecimal freeAmount;
    private BigDecimal amount;
    private Integer userId;
    private String coinName;
    private String coinImg;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getAddress()
    {
        return this.address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getContractAddr()
    {
        return this.contractAddr;
    }

    public void setContractAddr(String contractAddr)
    {
        this.contractAddr = contractAddr;
    }

    public BigDecimal getFreeAmount()
    {
        return this.freeAmount;
    }

    public void setFreeAmount(BigDecimal freeAmount)
    {
        this.freeAmount = freeAmount;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public Integer getUserId()
    {
        return this.userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String getCoinName()
    {
        return this.coinName;
    }

    public void setCoinName(String coinName)
    {
        this.coinName = coinName;
    }

    public String getCoinImg()
    {
        return this.coinImg;
    }

    public void setCoinImg(String coinImg)
    {
        this.coinImg = coinImg;
    }
}

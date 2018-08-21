package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;

public class WalletListInfo
{
    private Integer id;
    private String address;
    private String contractAddr;
    private BigDecimal amount;
    private String coinName;
    private String coinImg;
    private BigDecimal walletTotal;

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

    public BigDecimal getAmount()
    {
        return this.amount;
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

    public BigDecimal getWalletTotal()
    {
        return this.walletTotal;
    }

    public void setWalletTotal(BigDecimal walletTotal)
    {
        this.walletTotal = walletTotal;
    }
}

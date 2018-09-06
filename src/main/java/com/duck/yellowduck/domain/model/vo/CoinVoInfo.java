package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;

/**
 * 封装少量参数
 */
public class CoinVoInfo {

    private Integer id;

    //private Integer coinId;     //币种编号

    private String address;

    private String contractAddr;

    private BigDecimal amount;

    private String coinName;

    private String coinImg;

    private Integer userId;

    private BigDecimal walletTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /*public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinImg() {
        return coinImg;
    }

    public void setCoinImg(String coinImg) {
        this.coinImg = coinImg;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getWalletTotal() {
        return walletTotal;
    }

    public void setWalletTotal(BigDecimal walletTotal) {
        this.walletTotal = walletTotal;
    }
}

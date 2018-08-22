package com.duck.yellowduck.domain.model.vo;

/**
 * 封装币种信息
 */
public class WalletStatusVo {

    private String coinName;

    private Boolean status;


    public String getCoinName()
    {
        return this.coinName;
    }

    public void setCoinName(String coinName)
    {
        this.coinName = coinName;
    }

    public Boolean getStatus()
    {
        return this.status;
    }

    public void setStatus(Boolean status)
    {
        this.status = status;
    }
}

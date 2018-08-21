package com.duck.yellowduck.domain.model.vo;

public class CoinVo
{
    private Integer id;
    private String coinName;
    private String coinImg;
    private String address;
    private Integer status;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public String getAddress()
    {
        return this.address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getStatus()
    {
        return this.status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }
}

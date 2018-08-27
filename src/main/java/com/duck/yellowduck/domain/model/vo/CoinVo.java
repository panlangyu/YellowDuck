package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;


/**
 * 封装币种参数
 */
public class CoinVo {

    private Integer id;                 //编号

    private String name;                //币种全称

    private String coinName;            //币种名称

    private String address;             //币种地址

    private String coinImg;             //币种图像

    private BigDecimal marketPrice;     //市场价格

    private Integer status;             //状态



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }
}

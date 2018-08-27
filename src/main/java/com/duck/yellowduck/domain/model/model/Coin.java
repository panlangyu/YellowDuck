package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 币种表(Model)
 */
public class Coin {

    private Integer id;                 //编号

    private String name;                //币种全称

    private String coinName;            //币种名称

    private String address;             //币种地址

    private String coinImg;             //币种图像

    private BigDecimal marketPrice;     //市场价格

    private Date createTime;            //创建时间

    private Date updateTime;            //修改时间

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

    public Date getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
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

package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;
import java.util.Date;

public class Investment
{
    private Integer id;
    private Integer walletId;
    private BigDecimal interests;
    private BigDecimal recommend;
    private BigDecimal dynamicAward;
    private Date createTime;
    private Date updateTime;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getWalletId()
    {
        return this.walletId;
    }

    public void setWalletId(Integer walletId)
    {
        this.walletId = walletId;
    }

    public BigDecimal getInterests()
    {
        return this.interests;
    }

    public void setInterests(BigDecimal interests)
    {
        this.interests = interests;
    }

    public BigDecimal getRecommend()
    {
        return this.recommend;
    }

    public void setRecommend(BigDecimal recommend)
    {
        this.recommend = recommend;
    }

    public BigDecimal getDynamicAward()
    {
        return this.dynamicAward;
    }

    public void setDynamicAward(BigDecimal dynamicAward)
    {
        this.dynamicAward = dynamicAward;
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
}

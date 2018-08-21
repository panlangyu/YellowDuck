package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;

public class SystemStatistics
{
    private int userNumber;
    private BigDecimal recharge;
    private BigDecimal foward;
    private BigDecimal recommend;
    private BigDecimal interest;
    private BigDecimal active;

    public int getUserNumber()
    {
        return this.userNumber;
    }

    public void setUserNumber(int userNumber)
    {
        this.userNumber = userNumber;
    }

    public BigDecimal getRecharge()
    {
        return this.recharge;
    }

    public void setRecharge(BigDecimal recharge)
    {
        this.recharge = recharge;
    }

    public BigDecimal getFoward()
    {
        return this.foward;
    }

    public void setFoward(BigDecimal foward)
    {
        this.foward = foward;
    }

    public BigDecimal getRecommend()
    {
        return this.recommend;
    }

    public void setRecommend(BigDecimal recommend)
    {
        this.recommend = recommend;
    }

    public BigDecimal getInterest()
    {
        return this.interest;
    }

    public void setInterest(BigDecimal interest)
    {
        this.interest = interest;
    }

    public BigDecimal getActive()
    {
        return this.active;
    }

    public void setActive(BigDecimal active)
    {
        this.active = active;
    }
}

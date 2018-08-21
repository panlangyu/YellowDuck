package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易订单Model
 */
public class Transcation
{
    private Integer id;

    private Integer userId;

    private String coinName;

    private BigDecimal amount;

    private Integer txType;

    private String from;

    private String contractAddr;

    private String to;

    private String hash;

    private Integer txStatus;

    private Date createTime;

    private Date updateTime;

    private String remark;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getUserId()
    {
        return this.userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getCoinName()
    {
        return this.coinName;
    }

    public void setCoinName(String coinName)
    {
        this.coinName = coinName;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public Integer getTxType()
    {
        return this.txType;
    }

    public void setTxType(Integer txType)
    {
        this.txType = txType;
    }

    public String getFrom()
    {
        return this.from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getContractAddr()
    {
        return this.contractAddr;
    }

    public void setContractAddr(String contractAddr)
    {
        this.contractAddr = contractAddr;
    }

    public String getTo()
    {
        return this.to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getHash()
    {
        return this.hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public Integer getTxStatus()
    {
        return this.txStatus;
    }

    public void setTxStatus(Integer txStatus)
    {
        this.txStatus = txStatus;
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

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}

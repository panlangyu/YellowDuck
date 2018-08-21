package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 封装交易信息Vo
 */
public class TranscationVo {


    private Integer id;

    private Integer txType;

    private BigDecimal amount;

    private String from;

    private String contractAddr;

    private String to;

    private String hash;

    private Date createTime;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getTxType()
    {
        return this.txType;
    }

    public void setTxType(Integer txType)
    {
        this.txType = txType;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
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

    public Date getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


}

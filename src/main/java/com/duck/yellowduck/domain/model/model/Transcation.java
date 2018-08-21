package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易订单Model
 */
public class Transcation {

    private Integer id;                         //编号

    private Integer userId;                     //用户ID

    private String coinName;                    //币种

    private BigDecimal amount;                  //交易金额

    private Integer txType;                     //交易类型（1：转入，2转出）

    private String from;                        //转出地址

    private String contractAddr;                //合约币地址

    private String to;                          //收款地址

    private String hash;                        //交易ID

    private Integer txStatus;                   //交易状态（1：已提交，2：已完成）

    private Date createTime;                    //订单创建时间

    private Date updateTime;                    //订单修改时间

    private String remark;                      //备注

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

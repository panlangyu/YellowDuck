package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包Model
 */
public class RedEnvelopers {

    private Integer id;                     //编号

    private Integer userId;                 //用户编号

    private String coinName;                //币种名称

    private BigDecimal receivedAmount;      //已领数量(金额)

    private BigDecimal totalAmount;         //数量

    private Integer redLeadCount;           //已领个数

    private Integer redEnvelopersCount;     //红包个数

    private Integer status;                 //红包方式 1、转入 2、转出

    private Integer redEnvelopersStatus;    //红包状态 1、未领完 2、已领完 3、已过期

    private Integer redType;                //红包类型 1、普通红包 2、拼手气红包

    private Integer type;                   //类型 1、收到红包 2、发出红包  可有可无

    private String hash;                    //转账成功的hash

    private Date createTime;                //创建时间

    private Date updateTime;                //修改时间

    private String remark;                  //备注

    //private String nickName;                //昵称

    //private String headImg;                 //头像


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getRedLeadCount() {
        return redLeadCount;
    }

    public void setRedLeadCount(Integer redLeadCount) {
        this.redLeadCount = redLeadCount;
    }

    public Integer getRedEnvelopersCount() {
        return redEnvelopersCount;
    }

    public void setRedEnvelopersCount(Integer redEnvelopersCount) {
        this.redEnvelopersCount = redEnvelopersCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRedEnvelopersStatus() {
        return redEnvelopersStatus;
    }

    public void setRedEnvelopersStatus(Integer redEnvelopersStatus) {
        this.redEnvelopersStatus = redEnvelopersStatus;
    }

    public Integer getRedType() {
        return redType;
    }

    public void setRedType(Integer redType) {
        this.redType = redType;
    }


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }





}

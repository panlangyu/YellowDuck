package com.duck.yellowduck.domain.model.model;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户转账Model
 */
public class Transfer {


    private Integer id;             //编号

    private Integer userId;         //用户编号

    private String coinName;        //币种名称

    private BigDecimal amount;      //转账数量

    private Integer status;         //转账类型 1、转入 2、转出

    private Integer transferStatus; //金额状态 1、已领取 2、未领取 3、已过期

    private String hash;            //转账成功的hash值

    private Date createTime;        //创建时间

    private Date updateTime;        //修改时间

    private String remark;          //备注

    private String nickName;        //用户昵称

    private String headImg;         //头像


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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Integer transferStatus) {
        this.transferStatus = transferStatus;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}

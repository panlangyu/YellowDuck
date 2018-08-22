package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包记录Model
 */
public class RedPacketRecord {

    private Integer id;                     //编号

    private Integer userId;                 //用户编号

    private Integer redEnvelopersId;        //红包编号

    private String coinName;                //币种名称

    private BigDecimal amount;              //数量

    private Integer status;                 //红包方式 1、转入 2、转出

    private Date createTime;                //创建时间

    private Date updateTime;                //修改时间

    private String remark;                  //备注

    private String nickName;                //昵称

    private String headImg;                 //头像


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

    public Integer getRedEnvelopersId() {
        return redEnvelopersId;
    }

    public void setRedEnvelopersId(Integer redEnvelopersId) {
        this.redEnvelopersId = redEnvelopersId;
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

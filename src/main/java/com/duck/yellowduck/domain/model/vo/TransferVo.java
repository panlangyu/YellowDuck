package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 装配用户转账返回数据VO
 */
public class TransferVo {

    private Integer id;             //编号

    private Integer userId;         //用户编号

    private String coinName;        //币种名称

    private BigDecimal amount;      //转账数量

    //private Integer status;         //状态 1、已领取 2、未领取 3、已过期

    private Date createTime;        //创建时间

    //private Date updateTime;        //修改时间

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

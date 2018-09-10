package com.duck.yellowduck.domain.model.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户领取记录Model
 */
public class ReceiveRecord {

    private Integer id;                 //编号

    private String address;             //钱包地址

    //private Integer userId;             //用户编号

    private BigDecimal amount;          //领取数量

    private Date createTime;            //创建时间


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

   /* public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }*/

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
}

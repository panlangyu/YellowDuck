package com.duck.yellowduck.domain.model.model;

import java.util.Date;

/**
 * 合约币,用户关联Model
 */
public class ContractRelation {

    private Integer id;                     //编号

    private Integer coinId;                 //币种编号

    private String coinName;                //币种名称

    private String passwd;                  //密码

    private Date createTime;                //钱包创建时间

    private Date updateTime;                //钱包更新时间


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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





}

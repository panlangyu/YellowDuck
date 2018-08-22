package com.duck.yellowduck.domain.model.vo;


import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包记录VO
 */
public class RedPacketRecordVo {


    private Integer id;                     //编号

    private String coinName;                //币种名称

    //private BigDecimal receivedAmount;      //已领数量(金额)

    private BigDecimal amount;              //数量

    //private Integer redLeadCount;           //已领个数

    //private Integer redEnvelopersCount;     //红包个数

    private Date createTime;                //创建时间

    private String remark;                  //备注

    private String nickName;                //昵称

    private String headImg;                 //头像


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

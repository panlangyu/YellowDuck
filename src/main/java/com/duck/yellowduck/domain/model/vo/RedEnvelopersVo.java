package com.duck.yellowduck.domain.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 封装红包记录Vo
 */
public class RedEnvelopersVo {

    private Integer id;                     //编号

    //private String coinName;                //币种名称

    private Integer redLeadCount;           //已领个数

    private Integer redEnvelopersCount;     //红包个数

    private BigDecimal receivedAmount;      //已领数量(金额)

    private BigDecimal totalmount;          //红包总额数量

    private Date createTime;                //创建时间

    private String remark;                  //备注


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public BigDecimal getTotalmount() {
        return totalmount;
    }

    public void setTotalmount(BigDecimal totalmount) {
        this.totalmount = totalmount;
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




}

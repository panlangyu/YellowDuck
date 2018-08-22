package com.duck.yellowduck.domain.model.vo;


/**
 * 装配用户转账参数
 */
public class WalletVXUtilsVo {

    private String phone;               //用户手机号

    private String earnerPhone;         //被转账用户手机号

    private Integer userId;             //用户编号

    private String passwd;              //钱包密码

    private String hash;                //转账成功的hash值

    private String coinName;            //币种名称

    private String value;               //数量

    private String remark;              //备注

    private Boolean status;             //状态

    public String getPhone()
    {
        return this.phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEarnerPhone()
    {
        return this.earnerPhone;
    }

    public void setEarnerPhone(String earnerPhone)
    {
        this.earnerPhone = earnerPhone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCoinName()
    {
        return this.coinName;
    }

    public void setCoinName(String coinName)
    {
        this.coinName = coinName;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Boolean getStatus()
    {
        return this.status;
    }

    public void setStatus(Boolean status)
    {
        this.status = status;
    }
}

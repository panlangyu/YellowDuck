package com.duck.yellowduck.domain.model.vo;

public class WalletVXUtilsVo
{
    private String phone;
    private String earnerPhone;
    private String coinName;
    private String value;
    private String remark;
    private Boolean status;

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

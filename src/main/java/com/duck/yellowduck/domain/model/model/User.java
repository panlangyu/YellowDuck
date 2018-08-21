package com.duck.yellowduck.domain.model.model;

import java.util.Date;

/**
 * 用户Model
 */
public class User {

    private Integer id;

    private String userName;

    private String phone;

    private String passwd;

    private String headImg;

    private String nickName;

    private Integer sex;

    private Date synchronizationCreateTime;

    private Date synchronizationUpdateTime;

    private String remark;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public String getPasswd()
    {
        return this.passwd;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getHeadImg()
    {
        return this.headImg;
    }

    public void setHeadImg(String headImg)
    {
        this.headImg = headImg;
    }

    public String getNickName()
    {
        return this.nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public Integer getSex()
    {
        return this.sex;
    }

    public void setSex(Integer sex)
    {
        this.sex = sex;
    }

    public Date getSynchronizationCreateTime()
    {
        return this.synchronizationCreateTime;
    }

    public void setSynchronizationCreateTime(Date synchronizationCreateTime)
    {
        this.synchronizationCreateTime = synchronizationCreateTime;
    }

    public Date getSynchronizationUpdateTime()
    {
        return this.synchronizationUpdateTime;
    }

    public void setSynchronizationUpdateTime(Date synchronizationUpdateTime)
    {
        this.synchronizationUpdateTime = synchronizationUpdateTime;
    }

    public String getRemark()
    {
        return this.remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }


}

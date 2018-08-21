package com.duck.yellowduck.domain.model.vo;

import java.util.Date;

/**
 * 用户封装少量参数VO
 */
public class UserVo {

    private Integer id;

    private String userName;

    private String phone;

    private String headImg;

    private String nickName;

    private Integer sex;

    private Date synchronizationCreateTime;

    private Date synchronizationUpdateTime;

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

}

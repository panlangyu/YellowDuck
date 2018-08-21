package com.duck.yellowduck.domain.model.model;

import java.util.Date;

public class Dictionary
{
    private Integer id;
    private Integer parentId;
    private String name;
    private String value;
    private Integer code;
    private String type;
    private Integer sortSign;
    private Integer status;
    private Integer createAid;
    private Date createTime;
    private Integer updateAid;
    private Date updateTime;

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getParentId()
    {
        return this.parentId;
    }

    public void setParentId(Integer parentId)
    {
        this.parentId = parentId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Integer getCode()
    {
        return this.code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Integer getSortSign()
    {
        return this.sortSign;
    }

    public void setSortSign(Integer sortSign)
    {
        this.sortSign = sortSign;
    }

    public Integer getStatus()
    {
        return this.status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getCreateAid()
    {
        return this.createAid;
    }

    public void setCreateAid(Integer createAid)
    {
        this.createAid = createAid;
    }

    public Date getCreateTime()
    {
        return this.createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Integer getUpdateAid()
    {
        return this.updateAid;
    }

    public void setUpdateAid(Integer updateAid)
    {
        this.updateAid = updateAid;
    }

    public Date getUpdateTime()
    {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}

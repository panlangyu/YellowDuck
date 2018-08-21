package com.duck.yellowduck.domain.model.vo;

public class DictionaryVo
{
    private Integer id;
    private Integer parentId;
    private String name;
    private String value;
    private Integer code;
    private Integer sortSign;
    private Integer status;

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
}

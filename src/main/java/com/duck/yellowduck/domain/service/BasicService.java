package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.dao.BasicMapper;
import java.util.List;

abstract class BasicService<T>
{
    protected abstract BasicMapper basicMapper();

    public int save(T t)
    {
        int rows = basicMapper().insert(t);
        return rows;
    }

    public List<T> findList(T t)
    {
        return basicMapper().findList(t);
    }

    public void update(T t)
    {
        basicMapper().update(t);
    }

    public int updateByPrimaryKeySelective(T t)
    {
        return basicMapper().updateByPrimaryKeySelective(t);
    }

    public T selectByPrimaryKey(String l)
    {
        return (T)basicMapper().selectByPrimaryKey(l);
    }

    public int deleteByPrimaryKey(Long l)
    {
        return basicMapper().deleteByPrimaryKey(l);
    }
}

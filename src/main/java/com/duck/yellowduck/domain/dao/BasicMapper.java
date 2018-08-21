package com.duck.yellowduck.domain.dao;

import java.util.List;

public interface BasicMapper<T> {

    public  List<T> findList(T paramT);

    public  int deleteByPrimaryKey(Long paramLong);

    public  int insert(T paramT);

    public  T selectByPrimaryKey(String paramString);

    public  List<T> selectAll();

    public  int update(T paramT);

    public  int updateByPrimaryKeySelective(T paramT);

}

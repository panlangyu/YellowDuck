package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.vo.DictionaryVo;
import java.util.Dictionary;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public  interface DictionaryMapper
{
    public  List<Dictionary> selectDictionaryList(@Param("type") String type)
            throws Exception;

    public  List<DictionaryVo> selectDictionaryListById(@Param("id") Integer id, @Param("type") String type)
            throws Exception;

    public  int updateDictionaryValueById(@Param("id") int id, @Param("value") String value);
}

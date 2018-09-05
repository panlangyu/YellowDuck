package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.vo.DictionaryVo;
import java.util.Dictionary;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryMapper {

    List<Dictionary> selectDictionaryList(@Param("type") String type);


    List<DictionaryVo> selectDictionaryListById(@Param("id") Integer id, @Param("type") String type);


    int updateDictionaryValueById(@Param("id") int id, @Param("value") String value);
}

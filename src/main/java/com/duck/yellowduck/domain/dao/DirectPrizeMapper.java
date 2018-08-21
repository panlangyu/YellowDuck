package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.DirectPrize;
import com.duck.yellowduck.domain.model.vo.DirectPrizeVo;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public  interface DirectPrizeMapper
{
    public  Integer selectDirectPrizeCount(@Param("refereeId") Integer refereeId)
            throws Exception;

    public  List<DirectPrizeVo> selectDirectPrizeList();

    public  Integer insertDirectPrizeInfo(DirectPrize directPrize)
            throws Exception;

    public  Integer modifyDirectPrizeInfo(DirectPrize directPrize)
            throws Exception;
}

package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.ReceiveRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 用户领取记录Mapper
 */
@Repository
public interface ReceiveRecordMapper {

    /** 查询用户领取记录信息,可按地址查询单条 **/
    List<ReceiveRecord> selectReceiveRecordListInfo(@Param("currentPage")Integer currentPage,
                                                    @Param("currentSize")Integer currentSize,
                                                    @Param("address")String address);

}

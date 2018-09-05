package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Transfer;
import com.duck.yellowduck.domain.model.vo.TransferVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户转账Mapper
 */
@Repository
public interface TransferMapper {

    /** 用户转账记录 **/
    List<TransferVo> findUserTransferInfo(@Param("currentPage")Integer currentPage,@Param("currentSize")Integer currentSize,
                                          @Param("userId")Integer userId, @Param("startTime")String startTime,
                                          @Param("endTime")String endTime);

    /** 用户转账记录 转出 **/
    Integer insertTransferTurnTo(Transfer transfer);

    /** 用户转账记录 转入 **/
    Integer insertTransferToCharge(Transfer transfer);

}

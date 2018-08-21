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

    /**
     * 用户转账记录
     * @param currentPage
     * @param currentSize
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public List<TransferVo> findUserTransferInfo(@Param("currentPage")Integer currentPage,
                                                 @Param("currentSize")Integer currentSize,
                                                 @Param("userId")Integer userId,
                                                 @Param("startTime")String startTime,
                                                 @Param("endTime")String endTime)throws Exception;


    /**
     * 用户转账记录 转出
     * @param transfer
     * @return
     * @throws Exception
     */
    public Integer insertTransferTurnTo(Transfer transfer)throws Exception;


    /**
     * 用户转账记录 转入
     * @param transfer
     * @return
     * @throws Exception
     */
    public Integer insertTransferToCharge(Transfer transfer)throws Exception;

}

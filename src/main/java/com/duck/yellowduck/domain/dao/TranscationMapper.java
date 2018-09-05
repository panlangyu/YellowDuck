package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Transcation;
import com.duck.yellowduck.domain.model.vo.TranscationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 钱包交易订单Mapper
 */
@Repository
public interface TranscationMapper {

    List<TranscationVo> selectUserCoinTransactionList(@Param("currentPage") Integer currentPage, @Param("currentSize") Integer currentSize,
                                                      @Param("userId") Integer userId, @Param("coinName") String coinName);


    Integer insertWalletTurnOut(Transcation transcation);

    Integer insertWalletToChangeInto(Transcation transcation);

    Double selectUserCoinTurnToTotal(@Param("userId") Integer userId, @Param("startTime") String startTime,
                                     @Param("endTime") String endTime);


    Double selectUserCoinToChargeInfoTotal(@Param("userId") Integer userId, @Param("startTime") String startTime,
                                           @Param("endTime") String endTime) ;


    Map<String, Object> selectUserCoinTrunToChargeTotal(@Param("userId") Integer userId, @Param("startTime") String startTime,
                                                        @Param("endTime") String endTime);


    Map<String, Object> selectUserWalletCoinStraightOrInterest(@Param("userId") Integer userId, @Param("coinName") String coinName);


    List<TranscationVo> selectWalletUserCoinTransactionList(@Param("currentPage") Integer currentPage, @Param("currentSize") Integer currentSize,
                                                            @Param("userId") Integer userId, @Param("coinName") String coinName,
                                                            @Param("startTime") String startTime, @Param("endTime") String endTime);


    BigDecimal selectTranscationRechargeRecord(@Param("userId") Integer userId, @Param("coinId") Integer coinId);


}

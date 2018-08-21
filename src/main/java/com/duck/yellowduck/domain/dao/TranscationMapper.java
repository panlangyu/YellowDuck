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

    public  List<TranscationVo> selectUserCoinTransactionList(@Param("currentPage") Integer currentPage,
                                                              @Param("currentSize") Integer currentSize,
                                                              @Param("userId") Integer userId,
                                                              @Param("coinName") String coinName)throws Exception;


    public  Integer insertWalletTurnOut(Transcation transcation)throws Exception;


    public  Integer insertWalletToChangeInto(Transcation transcation)throws Exception;


    public  Double selectUserCoinTurnToTotal(@Param("userId") Integer userId,
                                             @Param("startTime") String startTime,
                                             @Param("endTime") String endTime)throws Exception;


    public  Double selectUserCoinToChargeInfoTotal(@Param("userId") Integer userId,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime) throws Exception;


    public  Map<String, Object> selectUserCoinTrunToChargeTotal(@Param("userId") Integer userId,
                                                                @Param("startTime") String startTime,
                                                                @Param("endTime") String endTime)throws Exception;


    public  Map<String, Object> selectUserWalletCoinStraightOrInterest(@Param("userId") Integer userId, @Param("coinName") String coinName)throws Exception;


    public  List<TranscationVo> selectWalletUserCoinTransactionList(@Param("currentPage") Integer currentPage,
                                                                    @Param("currentSize") Integer currentSize,
                                                                    @Param("userId") Integer userId,
                                                                    @Param("coinName") String coinName,
                                                                    @Param("startTime") String startTime,
                                                                    @Param("endTime") String endTime)throws Exception;


    public  BigDecimal selectTranscationRechargeRecord(@Param("userId") Integer userId,
                                                       @Param("coinId") Integer coinId)throws Exception;


}

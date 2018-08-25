package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Wallet;
import com.duck.yellowduck.domain.model.vo.WalletStatusVo;
import com.duck.yellowduck.domain.model.vo.WalletUtilsVo;
import com.duck.yellowduck.domain.model.vo.WalletVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * 钱包Mapper
 */
@Repository
public interface WalletMapper {


    public BigDecimal selectUserWalletTotal(@Param("userId") Integer userId) throws Exception;


    public List<WalletVo> selectUserWalletInfo(@Param("currentPage") Integer currentPage,
                                                @Param("currentSize") Integer currentSize,
                                                @Param("userId") Integer userId,
                                                @Param("coinName") String coinName)throws Exception;


    public List<Wallet> lockWalletTable() throws Exception;

    public Wallet selectUserWalletCoinById(WalletUtilsVo walletUtilsVo)throws Exception;


    public Wallet selectUserWalletCoinByAddress(WalletUtilsVo walletUtilsVo)throws Exception;


    public Integer modifyWalletTurnOut(Wallet wallet) throws Exception;


    public Integer modifyWalletToChangeInto(Wallet wallet)throws Exception;


    public Wallet selectUserWalletByCoinId(@Param("userId") Integer userId,
                                            @Param("coinName") String coinName)throws Exception;


    public Map<String, Object> selectYesterdayProfit(@Param("userId") Integer userId,
                                                     @Param("coinId") Integer coinId)throws Exception;


    public Integer modifyWalletDepositToChangeInto(Wallet wallet)throws Exception;


    public Integer modifyWalletDepositTurnOut(Wallet wallet)throws Exception;


    public Integer insertUserWalletInfo(List<Wallet> list)throws Exception;


    public List<Wallet> selectUserWalletInterest()throws Exception;


    public Integer modifyUserWalletInterest(Wallet wallet)throws Exception;


    public Integer insertWalletInfo(Wallet wallet) throws Exception;


    public String findWalletAddressByUserId(@Param("userId") Integer userId,
                                            @Param("coinName") String coinName)throws Exception;


    public Wallet findWalletByUserIdAndAddress(@Param("userId") Integer userId,
                                               @Param("contractAddr") String contractAddr)throws Exception;


    public List<Wallet> findUserWalletInfo(@Param("userId") Integer userId)throws Exception;

    /**
     * 查询用户下的币种,只读取币种名称
     * @param userId
     * @return
     * @throws Exception
     */
    public List<WalletStatusVo> findWalletListInfo(@Param("userId") Integer userId)throws Exception;

}

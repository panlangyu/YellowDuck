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


    /**
     * 给当前操作的行加锁(共享锁)
     * @param id
     * @return
     * @throws Exception
     */
    public List<Wallet> lockWalletTable(@Param("id")Integer id) throws Exception;

    public Wallet selectUserWalletCoinById(WalletUtilsVo walletUtilsVo)throws Exception;


    public Wallet selectUserWalletCoinByAddress(WalletUtilsVo walletUtilsVo)throws Exception;


    public Integer modifyWalletTurnOut(Wallet wallet) throws Exception;


    public Integer modifyWalletToChangeInto(Wallet wallet)throws Exception;

    /**
     * 查询用户ETH单条信息
     * @param userId
     * @param id
     * @return
     * @throws Exception
     */
    public Wallet selectUserWalletETHAddress(@Param("userId") Integer userId,
                                             @Param("id") Integer id)throws Exception;

    /**
     * 查询用户ETH下合约币的单条信息
     * @param userId
     * @param id
     * @return
     * @throws Exception
     */
    public Wallet selectUserWalletContractAddrInfo(@Param("userId") Integer userId,
                                                   @Param("id") Integer id)throws Exception;


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

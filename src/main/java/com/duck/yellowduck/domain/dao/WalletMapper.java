package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Wallet;
import com.duck.yellowduck.domain.model.vo.CoinVoInfo;
import com.duck.yellowduck.domain.model.vo.WalletStatusVo;
import com.duck.yellowduck.domain.model.vo.WalletVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 * 钱包Mapper
 */
@Repository
public interface WalletMapper {


    /** 查询用户币种信息 **/
    List<CoinVoInfo> selectUserWalletInfo(@Param("currentPage") Integer currentPage, @Param("currentSize") Integer currentSize,
                                          @Param("userId") Integer userId, @Param("coinName") String coinName);

    /** 查询币种详情ETH币种信息 **/
    List<CoinVoInfo> selectETHCoinInfoById(@Param("currentPage") Integer currentPage, @Param("currentSize") Integer currentSize,
                                           @Param("userId") Integer userId,@Param("coinName") String coinName, @Param("id") Integer id);

    /** 查询币种详情合约币信息 **/
    List<CoinVoInfo> selectContractCoinInfoById(@Param("currentPage") Integer currentPage, @Param("currentSize") Integer currentSize,
                                                @Param("userId") Integer userId,@Param("coinName") String coinName, @Param("id") Integer id);

    /** 给当前操作的行加锁(共享锁) **/
    List<Wallet> lockWalletTable(@Param("id")Integer id) ;

    /** 查询用户ETH单条信息 **/
    Wallet selectUserWalletETHAddress(@Param("userId") Integer userId, @Param("id") Integer id);

    /** 查询用户ETH下合约币的单条信息 **/
    Wallet selectUserWalletContractAddrInfo(@Param("userId") Integer userId, @Param("id") Integer id);

    List<Wallet> selectUserWalletInterest();

    Integer modifyUserWalletInterest(Wallet wallet);

    /** 添加ETH钱包信息 **/
    Integer insertWalletInfo(Wallet wallet) ;

    /** 查询用户ETH的信息 **/
    String findWalletAddressByUserId(@Param("userId") Integer userId, @Param("coinName") String coinName);


    Wallet findWalletByUserIdAndAddress(@Param("userId") Integer userId,@Param("contractAddr") String contractAddr);

    /** 用户钱包信息 **/
    List<Wallet> findUserWalletInfo(@Param("userId") Integer userId);

    /** ETH信息 **/
    WalletStatusVo findWalletETHListInfo(@Param("userId") Integer userId);

    /** 查询用户下的币种信息,基本信息 **/
    List<WalletStatusVo> findWalletListInfo(@Param("userId") Integer userId);

}

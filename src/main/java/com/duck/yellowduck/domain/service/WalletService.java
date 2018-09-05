package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.WalletUtilsVo;

/**
 * 钱包Service
 */
public interface WalletService {

    /** 创建ETH钱包 **/
    ApiResponseResult createWalletInfo(User user) ;

    /** 查询用户钱包信息 **/
    ApiResponseResult findUserWalletList(Integer currentPage,Integer currentSize,String phone,Integer id, String coinName) ;

    /** 用户提币 **/
    ApiResponseResult modifyWithdrawMoney(WalletUtilsVo walletUtilsVo) ;

    /** 添加合约币信息 **/
    ApiResponseResult queryContractAddr(String phone, String contractAddr);

    /** 查询所有账户 **/
    ApiResponseResult queryAccountList();

    /** 查询阻塞数 **/
    ApiResponseResult blockNumber();

    /** 查询用户下的币种信息,基本信息 **/
    ApiResponseResult findWalletListInfo(String phone);

    /** 查询用户ETH地址 **/
    ApiResponseResult findWalletAddressByUserId(String phone);

}

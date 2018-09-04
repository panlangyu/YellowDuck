package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.model.Wallet;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.WalletUtilsVo;
import com.duck.yellowduck.domain.model.vo.WalletVXUtilsVo;

/**
 * 钱包Service
 */
public interface WalletService {


    public ApiResponseResult selectUserWalletTotal(Integer userId)throws Exception;


    public ApiResponseResult selectUserWalletCoinList(Integer currentPage, Integer currentSize, Integer userId, String coinName)
            throws Exception;

    public ApiResponseResult modifyWalletTurnOut(Wallet paramWallet)
            throws Exception;

    public ApiResponseResult selectUserWalletCoinStraightOrInterest(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3)
            throws Exception;

    public ApiResponseResult selectYesterdayProfit(Integer paramInteger1, Integer paramInteger2)
            throws Exception;

    public ApiResponseResult modifyWalletDepositToChangeInto(Wallet wallet)
            throws Exception;

    public ApiResponseResult modifyWalletDepositTurnOut(Wallet wallet)
            throws Exception;

    public ApiResponseResult modifyChargeMoneyInfo(Wallet wallet)
            throws Exception;

    public ApiResponseResult createWalletInfo(User user)
            throws Exception;

    public ApiResponseResult findUserWalletList(Integer currentPage,Integer currentSize,String phone,Integer id, String coinName)
            throws Exception;

    public ApiResponseResult modifyWithdrawMoney(WalletUtilsVo walletUtilsVo)
            throws Exception;

    public ApiResponseResult queryContractAddr(String phone, String contractAddr)
            throws Exception;

    public ApiResponseResult queryAccountList()
            throws Exception;

    public ApiResponseResult blockNumber();

    public ApiResponseResult queryUserWalletInfo(String paramString1, String paramString2)
            throws Exception;

    /**
     * 查询用户下的币种,只读取币种名称
     * @param phone
     * @return
     * @throws Exception
     */
    public ApiResponseResult findWalletListInfo(String phone)throws Exception;



}

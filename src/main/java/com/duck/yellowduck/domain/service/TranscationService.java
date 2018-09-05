package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;

/**
 * 交易记录Service
 */
public interface TranscationService {

    ApiResponseResult selectUserCoinTransactionList(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, String paramString) throws Exception;

    ApiResponseResult selectUserCoinTransactionListInfo(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, String paramString) throws Exception;

    ApiResponseResult selectUserCoinTransactionTotal(Integer paramInteger, String paramString) throws Exception;

    ApiResponseResult selectUserCoinTrunToChargeTotal(Integer paramInteger, String paramString) throws Exception;

    ApiResponseResult selectWalletUserCoinTransactionList(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, String paramString1, String paramString2) throws Exception;

    ApiResponseResult findUserTransactionList(Integer paramInteger1, Integer paramInteger2, String paramString1, String paramString2) throws Exception;

}

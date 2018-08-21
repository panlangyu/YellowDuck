package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;

public abstract interface TranscationService
{
    public abstract ApiResponseResult selectUserCoinTransactionList(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, String paramString)
            throws Exception;

    public abstract ApiResponseResult selectUserCoinTransactionListInfo(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, String paramString)
            throws Exception;

    public abstract ApiResponseResult selectUserCoinTransactionTotal(Integer paramInteger, String paramString)
            throws Exception;

    public abstract ApiResponseResult selectUserCoinTrunToChargeTotal(Integer paramInteger, String paramString)
            throws Exception;

    public abstract ApiResponseResult selectWalletUserCoinTransactionList(Integer paramInteger1, Integer paramInteger2, Integer paramInteger3, String paramString1, String paramString2)
            throws Exception;

    public abstract ApiResponseResult findUserTransactionList(Integer paramInteger1, Integer paramInteger2, String paramString1, String paramString2)
            throws Exception;
}

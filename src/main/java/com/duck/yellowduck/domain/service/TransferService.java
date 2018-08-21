package com.duck.yellowduck.domain.service;


import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.WalletVXUtilsVo;


/**
 * 用户转账ServiceImpl
 */
public interface TransferService {

    /**
     * 用户转账信息
     * @param walletVXUtilsVo
     * @return
     * @throws Exception
     */
    public ApiResponseResult chatAndTransfer(WalletVXUtilsVo walletVXUtilsVo) throws Exception;


    /**
     * 用户转账记录
     * @param currentPage
     * @param currentSize
     * @param phone
     * @param startTime
     * @return
     * @throws Exception
     */
    public ApiResponseResult findUserTransferInfo(Integer currentPage, Integer currentSize,
                                                 String phone, String startTime)throws Exception;




}

package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.WalletVXUtilsVo;

/**
 * 用户转账ServiceImpl
 */
public interface TransferService {

     /** 用户转账信息 **/
     ApiResponseResult chatAndTransfer(WalletVXUtilsVo walletVXUtilsVo);

     /** 用户转账记录 **/
     ApiResponseResult findUserTransferInfo(Integer currentPage, Integer currentSize,String phone, String startTime);

}

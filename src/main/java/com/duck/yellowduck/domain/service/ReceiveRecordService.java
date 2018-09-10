package com.duck.yellowduck.domain.service;


import com.duck.yellowduck.domain.model.response.ApiResponseResult;

/**
 * 用户领取记录Service
 */
public interface ReceiveRecordService {


    /** 查询用户领取记录信息,可按地址查询单条 **/
    ApiResponseResult findReceiveRecordListInfo(Integer currentPage,Integer currentSize, String address);

}

package com.duck.yellowduck.controller;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.service.TranscationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@Api(value="钱包业务接口", tags={"transaction service"})
public class TranscationController
{
    @Autowired
    private TranscationService transcationService;              //钱包交易订单Service

    @ApiOperation(value="查询用户钱包下币种详情订单记录", notes="根据userId和coinType查询订单记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currentPage", value="当前页码", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="currentSize", value="页面容量", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="userId", value="用户编号", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="coinType", value="币种名称", dataType="String", paramType="query", required=true)
    })
    @RequestMapping(value={"/queryUserCoinTransactionList"}, method= RequestMethod.GET)
    public ApiResponseResult queryUserCoinTransactionList(@RequestParam("currentPage") Integer currentPage,
                                                          @RequestParam("currentSize") Integer currentSize,
                                                          @RequestParam("userId") Integer userId,
                                                          @RequestParam("coinType") String coinType) {
        ApiResponseResult apiResponse = new ApiResponseResult();
        try
        {
            apiResponse = transcationService.selectUserCoinTransactionList(currentPage, currentSize, userId, coinType);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ApiResponseResult.build(2004, "error", "出现异常", "");
        }
        return apiResponse;
    }

    @ApiOperation(value="查询用户钱包下币种详情订单记录,按日期条件查询", notes="根据userId 或 startTime查询订单记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currentPage", value="当前页码", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="currentSize", value="页面容量", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="userId", value="用户编号", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="startTime", value="月份条件", dataType="String", paramType="query", required=false)
    })
    @RequestMapping(value={"/queryUserCoinTransactionListInfo"}, method= RequestMethod.GET)
    public ApiResponseResult queryUserCoinTransactionListInfo(@RequestParam("currentPage") Integer currentPage,
                                                              @RequestParam("currentSize") Integer currentSize,
                                                              @RequestParam("userId") Integer userId,
                                                              @RequestParam(value="startTime", required=false) String startTime) {

        ApiResponseResult apiResponse = new ApiResponseResult();
        try
        {
            apiResponse = transcationService.selectUserCoinTransactionListInfo(currentPage, currentSize, userId, startTime);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ApiResponseResult.build(2004, "error", "出现异常", "");
        }
        return apiResponse;
    }

    @ApiOperation(value="查询用户币种交易记录 收入 和 支出", notes="收入和支出")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户编号", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="startTime", value="开始时间", dataType="String", paramType="query", required=false)})
    @RequestMapping(value={"/queryUserCoinTransactionTotal"}, method= RequestMethod.GET)
    public ApiResponseResult queryUserCoinTransactionTotal(@RequestParam("userId") Integer userId,
                                                           @RequestParam(value="startTime", required=false) String startTime)
    {
        ApiResponseResult apiResponse = new ApiResponseResult();
        try
        {
            apiResponse = transcationService.selectUserCoinTrunToChargeTotal(userId, startTime);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ApiResponseResult.build(2004, "error", "出现异常", "");
        }
        return apiResponse;
    }

    @ApiOperation(value="查询(钱包管理)用户币种交易记录", notes="根据userId和coinType查询钱包管理币种交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currentPage", value="当前页码", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="currentSize", value="页面容量", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="userId", value="用户编号", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="coinType", value="币种名称", dataType="String", paramType="query", required=true)
    })
    @RequestMapping(value={"/queryWalletUserCoinTransactionList"}, method= RequestMethod.GET)
    public ApiResponseResult queryWalletUserCoinTransactionList(@RequestParam("currentPage") Integer currentPage,
                                                                @RequestParam("currentSize") Integer currentSize,
                                                                @RequestParam("userId") Integer userId,
                                                                @RequestParam("coinType") String coinType)
    {
        ApiResponseResult apiResponse = new ApiResponseResult();
        try
        {
            apiResponse = transcationService.selectWalletUserCoinTransactionList(currentPage, currentSize, userId, coinType, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ApiResponseResult.build(2004, "error", "出现异常", "");
        }
        return apiResponse;
    }

    @ApiOperation(value="查询用户币种交易记录", notes="根据phone和coinName查询钱包币种交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currentPage", value="当前页码", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="currentSize", value="页面容量", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="phone", value="手机号", dataType="String", paramType="query", required=true),
            @ApiImplicitParam(name="coinName", value="币种名称", dataType="String", paramType="query", required=true)
    })
    @RequestMapping(value={"/queryUserTransactionList"}, method= RequestMethod.GET)
    public ApiResponseResult queryUserTransactionList(@RequestParam("currentPage") Integer currentPage,
                                                      @RequestParam("currentSize") Integer currentSize,
                                                      @RequestParam("phone") String phone,
                                                      @RequestParam("coinName") String coinName){

        ApiResponseResult apiResponseResult = new ApiResponseResult();
        try {
            
            apiResponseResult = transcationService.findUserTransactionList(currentPage, currentSize, phone, coinName);
        }
        catch (Exception e) {
            e.printStackTrace();

            return ApiResponseResult.build(2011, "error", "出现异常", "");
        }
        return apiResponseResult;
    }
}

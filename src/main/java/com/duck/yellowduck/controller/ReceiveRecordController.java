package com.duck.yellowduck.controller;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.service.ReceiveRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receive")
@Api(value="领取记录接口", tags={"用户领取功能"})
public class ReceiveRecordController {

    @Autowired
    private ReceiveRecordService receiveRecordService;                //用户领取记录Service

    @ApiOperation(value="查询用户钱包币种列表", notes="根据用户编号查询用户钱包信息,调用第三方接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currentPage", value="当前页码", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="currentSize", value="页面容量", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="address", value="钱包地址", dataType="String", paramType="query", required=false)
    })
    @RequestMapping(value={"/queryReceiveRecordListInfo"}, method= RequestMethod.GET)
    public ApiResponseResult queryReceiveRecordListInfo(@RequestParam("currentPage")Integer currentPage,
                                                         @RequestParam("currentSize")Integer currentSize,
                                                         @RequestParam(value = "address",required = false)String address) {

        return receiveRecordService.findReceiveRecordListInfo(currentPage,currentSize,address);
    }

    @ApiOperation(value="查询用户是否领取过", notes="按手机号去查询")
    @ApiImplicitParam(name="phone", value="手机号", dataType="String", paramType="query", required=true)
    @RequestMapping(value={"/queryUserReceiveIsTrue"}, method=RequestMethod.GET)
    public ApiResponseResult queryUserReceiveIsTrue(@RequestParam("phone") String phone){

        return receiveRecordService.findReceiveIsTrue(phone);
    }



}

package com.duck.yellowduck.controller;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.WalletVXUtilsVo;
import com.duck.yellowduck.domain.service.TransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户转账Controller
 */
@RestController
@RequestMapping("transfer")
@Api(value="聊天转账", tags={"用户聊天转账"})
public class TransferController {

    @Autowired
    private TransferService transferService;                //用户转账Service


    @ApiOperation(value="用户聊天转账", notes="转账")
    @ApiImplicitParam(name="wallet", value="装配聊天转账wallet", dataType="Wallet")
    @RequestMapping(value={"/chatAndTransfer"}, method= RequestMethod.POST)
    public ApiResponseResult chatAndTransfer(@RequestBody WalletVXUtilsVo wallet) {

        return transferService.chatAndTransfer(wallet);
    }


    @ApiOperation(value="转账记录", notes="转账")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currentPage", value="当前页码", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="currentSize", value="页面容量", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="phone", value="手机号", dataType="String", paramType="query", required=true),
            @ApiImplicitParam(name="startTime", value="币种名称", dataType="String", paramType="query", required=false)
    })
    @RequestMapping(value={"/queryUserTransferList"}, method= RequestMethod.GET)
    public ApiResponseResult queryUserTransferList(@RequestParam("currentPage")Integer currentPage,
                                                   @RequestParam("currentSize")Integer currentSize,
                                                   @RequestParam("phone")String phone,
                                                   @RequestParam(value="startTime",required = false)String startTime){

        return transferService.findUserTransferInfo(currentPage,currentSize,phone,startTime);
    }



}

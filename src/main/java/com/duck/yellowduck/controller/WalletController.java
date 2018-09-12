package com.duck.yellowduck.controller;

import com.duck.yellowduck.domain.enums.WalletEnum;
import com.duck.yellowduck.domain.exception.WalletException;
import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.WalletUtilsVo;
import com.duck.yellowduck.domain.service.WalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@Api(value="钱包业务接口", tags={"钱包功能"})
public class WalletController {


    @Autowired
    private WalletService walletService;                //钱包Service


    @ApiOperation(value="用户创建钱包", notes="创建钱包")
    @ApiImplicitParam(name="user", value="用户对象user", dataType="User")
    @RequestMapping(value="/createWalletInfo", method=RequestMethod.POST)
    public ApiResponseResult createWalletInfo(@RequestBody User user,
                                              HttpServletRequest request) {

        return walletService.createWalletInfo(user);
    }

    @ApiOperation(value="查询用户钱包币种列表", notes="根据用户编号查询用户钱包信息,调用第三方接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currentPage", value="当前页码", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="currentSize", value="页面容量", dataType="Integer", paramType="query", required=true),
            @ApiImplicitParam(name="phone", value="手机号", dataType="String", paramType="query", required=true),
            @ApiImplicitParam(name="id", value="币种编号", dataType="Integer", paramType="query", required=false),
            @ApiImplicitParam(name="coinName", value="币种名称", dataType="String", paramType="query", required=false)
    })
    @RequestMapping(value="/queryUserWalletList", method=RequestMethod.GET)
    public ApiResponseResult queryUserWalletList(@RequestParam("currentPage") Integer currentPage,
                                                 @RequestParam("currentSize") Integer currentSize,
                                                 @RequestParam("phone") String phone,
                                                 @RequestParam(value="id",required = false) Integer id,
                                                 @RequestParam(value="coinName", required=false) String coinName) {

        if((id == null || id <= 0) && (coinName!=null && !coinName.equals(""))){

            throw new WalletException(WalletEnum.WALLET_IDANDNAME_NULL);
        }
        if((id != null && id > 0) && (coinName == null || coinName.equals(""))){

            throw new WalletException(WalletEnum.WALLET_IDANDNAME_NULL);
        }
        return walletService.findUserWalletList(currentPage,currentSize,phone,id,coinName);
    }

    @ApiOperation(value="用户提币", notes="调用第三方转账")
    @ApiImplicitParam(name="wallet", value="钱包对象wallet", dataType="Wallet")
    @RequestMapping(value="/modifyWithdrawMoney", method=RequestMethod.POST)
    public ApiResponseResult modifyWithdrawMoney(@RequestBody WalletUtilsVo wallet,
                                                 HttpServletRequest request){

        //验证手机号是否输入
        if(wallet.getPhone() == null || wallet.getPhone().equals("") || wallet.getPhone().equals("\"\"") || wallet.getPhone().equals("''")){

            throw new WalletException(WalletEnum.WALLET_PHONE_NULL);
        }
        //验证钱包编号是否输入
        if(wallet.getId() == null || wallet.getId() <= 0 ){

            throw new WalletException(WalletEnum.WALLET_ID_NULL);
        }
        //验证转入地址是否输入
        if(wallet.getAddress() == null || wallet.getAddress().equals("") || wallet.getAddress().equals("\"\"") || wallet.getAddress().equals("''")){

            throw new WalletException(WalletEnum.WALLET_ADDRESS_NULL);
        }
        //验证数量是否输入
        if(wallet.getValue() == null || wallet.getValue().equals("") || wallet.getValue().equals("\"\"") || wallet.getValue().equals("''")){

            throw new WalletException(WalletEnum.WALLET_VALUE_NULL);
        }

        return walletService.modifyWithdrawMoney(wallet);
    }

    @ApiOperation(value="查询合约币信息", notes="合约币")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone", value="手机号", dataType="String", paramType="query", required=true),
            @ApiImplicitParam(name="contractAddr", value="合约币地址", dataType="String", paramType="query", required=true)
    })
    @RequestMapping(value="/queryContractAddr", method=RequestMethod.GET)
    public ApiResponseResult queryContractAddr(@RequestParam("phone") String phone,
                                               @RequestParam("contractAddr") String contractAddr) {

        //验证手机号是否输入
        if(phone == null || phone.equals("") || phone.equals("\"\"") || phone.equals("''")){

            throw new WalletException(WalletEnum.WALLET_PHONE_NULL);
        }
        //验证合约币地址是否输入
        if(contractAddr == null || contractAddr.equals("") || contractAddr.equals("\"\"") || contractAddr.equals("''")){

            throw new WalletException(WalletEnum.WALLET_CONTRACTADDR_NULL);
        }

        return walletService.queryContractAddr(phone, contractAddr);
    }

    @ApiOperation(value="查询所有账户", notes="所有账户")
    @RequestMapping(value="/queryAccountInfo", method= RequestMethod.GET)
    public ApiResponseResult queryAccountInfo() {

        return walletService.queryAccountList();
    }

    @ApiOperation(value="查询阻塞数信息", notes="阻塞数")
    @RequestMapping(value="/blockNumber", method=RequestMethod.GET)
    public ApiResponseResult blockNumber(){

        return walletService.blockNumber();
    }

    @ApiOperation(value="查询用户下拥有的币种", notes="币种名称")
    @ApiImplicitParam(name="phone", value="手机号", dataType="String", paramType="query", required=true)
    @RequestMapping(value="/queryWalletListInfo", method=RequestMethod.GET)
    public ApiResponseResult queryWalletListInfo(@RequestParam("phone")String phone){

        //验证手机号是否输入
        if(phone == null || phone.equals("") || phone.equals("\"\"") || phone.equals("''")){

            throw new WalletException(WalletEnum.WALLET_PHONE_NULL);
        }

        return walletService.findWalletListInfo(phone);
    }


    @ApiOperation(value="查询用户ETH钱包地址", notes="ETH钱包地址")
    @ApiImplicitParam(name="phone", value="手机号", dataType="String", paramType="query", required=true)
    @RequestMapping(value="/queryWalletAddressByUserId", method=RequestMethod.GET)
    public ApiResponseResult queryWalletAddressByUserId(@RequestParam("phone")String phone){

        //验证手机号是否输入
        if(phone == null || phone.equals("") || phone.equals("\"\"") || phone.equals("''")){

            throw new WalletException(WalletEnum.WALLET_PHONE_NULL);
        }

        return walletService.findWalletAddressByUserId(phone);
    }

    @ApiOperation(value="查询用户ETH钱包地址", notes="ETH钱包地址")
    @ApiImplicitParam(name="passwd", value="资金密码", dataType="String", paramType="query", required=true)
    @RequestMapping(value="/rsaShow", method=RequestMethod.GET)
    public ApiResponseResult rsaShow(@RequestParam("passwd")String passwd){

        return walletService.rsaShow(passwd);
    }


}

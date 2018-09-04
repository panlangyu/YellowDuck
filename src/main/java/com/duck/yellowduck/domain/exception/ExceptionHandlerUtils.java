package com.duck.yellowduck.domain.exception;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常的处理
 */
@ControllerAdvice
public class ExceptionHandlerUtils {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtils.class);

    //自定义接收钱包抛出异常
    @ExceptionHandler(value = WalletException.class)
    @ResponseBody
    public ApiResponseResult walletException(WalletException e) {

        logger.error("【自定义异常】{}：", e.getWalletEnum().getMessage());
        return ApiResponseResult.build(e.getWalletEnum().getCode(),"error",e.getWalletEnum().getMessage(),"");
    }

    //空值异常
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ApiResponseResult nullPointerException(NullPointerException e) {

        logger.error("【系统异常】{}：", e.getMessage());
        return ApiResponseResult.build(1001,"error","系统异常,请联系管理员","");
    }

    //所有异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiResponseResult exception(Exception e) {
        //System.out.println(e.getMessage());
        //e.printStackTrace();
        //if(e instanceof  WalletException){
            //WalletException walletException = (WalletException)e;
        logger.error("【系统异常】{}：", e.getMessage());
        return ApiResponseResult.build(1001,"error","系统异常,请联系管理员","");
        //}
    }





}

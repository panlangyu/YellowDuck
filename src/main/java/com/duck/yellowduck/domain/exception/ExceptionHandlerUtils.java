package com.duck.yellowduck.domain.exception;

import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义业务异常的处理
 */
@RestController
@ControllerAdvice
public class ExceptionHandlerUtils {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandlerUtils.class);

    //自定义接收用户抛出异常
    @ExceptionHandler(value = UserException.class)
    public ApiResponseResult userException(UserException ex) {

        this.logger.error("【自定义异常用户功能】 {} : "+ ex.getUserEnum().getMessage());
        return ApiResponseResult.build(ex.getUserEnum().getCode(),"error",ex.getUserEnum().getMessage(),null);
    }

    //自定义接收钱包抛出异常
    @ExceptionHandler(value = WalletException.class)
    public ApiResponseResult walletException(WalletException ex) {

        this.logger.error("【自定义异常钱包功能】 {} :  "+ ex.getWalletEnum().getMessage());
        return ApiResponseResult.build(ex.getWalletEnum().getCode(),"error",ex.getWalletEnum().getMessage(),null);
    }

    //自定义接收聊天转账抛出异常
    @ExceptionHandler(value = TransferException.class)
    public ApiResponseResult transferException(TransferException ex) {

        this.logger.error("【自定义异常聊天转账】 {} : "+ex.getTransferEnum().getMessage());
        return ApiResponseResult.build(ex.getTransferEnum().getCode(),"error",ex.getTransferEnum().getMessage(),null);
    }







}

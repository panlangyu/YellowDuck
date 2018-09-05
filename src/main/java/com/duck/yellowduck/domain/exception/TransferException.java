package com.duck.yellowduck.domain.exception;

import com.duck.yellowduck.domain.enums.TransferEnum;

/**
 * 用户异常处理类
 */
public class TransferException extends RuntimeException {

    private TransferEnum transferEnum;

    public TransferException(){

    }

    public TransferException(TransferEnum transferEnum) {

        //super(walletEnum.getMessage());
        this.transferEnum = transferEnum;
    }


    public TransferEnum getTransferEnum() {
        return transferEnum;
    }

    public void setTransferEnum(TransferEnum transferEnum) {
        this.transferEnum = transferEnum;
    }
}

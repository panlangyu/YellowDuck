package com.duck.yellowduck.domain.exception;

import com.duck.yellowduck.domain.enums.ReceiveRecordEnum;

/**
 * 用户异常处理类
 */
public class ReceiveRecordException extends RuntimeException {

    private ReceiveRecordEnum receiveRecordEnum;

    public ReceiveRecordException(){

    }

    public ReceiveRecordException(ReceiveRecordEnum receiveRecordEnum) {
        this.receiveRecordEnum = receiveRecordEnum;
    }


    public ReceiveRecordEnum getReceiveRecordEnum() {
        return receiveRecordEnum;
    }

    public void setReceiveRecordEnum(ReceiveRecordEnum receiveRecordEnum) {
        this.receiveRecordEnum = receiveRecordEnum;
    }
}

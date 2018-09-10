package com.duck.yellowduck.domain.enums;

/**
 * 用户枚举类，异常提示
 */
public enum ReceiveRecordEnum {

    RECEIVE_NOT_INFO(1001,"未查询到用户领取记录");



    private Integer code;               //状态码
    private String message;             //提示信息


    private ReceiveRecordEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}

package com.duck.yellowduck.domain.enums;

/**
 * 用户枚举类，异常提示
 */
public enum UserEnum {

    USER_NOT_INFO(1001,"用户不存在"),
    USER_INSERT_FAIL(1002,"添加失败"),
    USER_MODIFY_FAIL(1005,"修改失败"),
    USER_RECEIVE_INFO(1006,"已领取过该福利");



    private Integer code;               //状态码
    private String message;             //提示信息


    private UserEnum(Integer code, String message) {
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

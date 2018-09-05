package com.duck.yellowduck.domain.exception;

import com.duck.yellowduck.domain.enums.UserEnum;

/**
 * 用户异常处理类
 */
public class UserException extends RuntimeException {

    private UserEnum userEnum;

    public UserException(){

    }

    public UserException(UserEnum userEnum) {
        //super(walletEnum.getMessage());
        this.userEnum = userEnum;
    }


    public UserEnum getUserEnum() {
        return userEnum;
    }

    public void setUserEnum(UserEnum userEnum) {
        this.userEnum = userEnum;
    }


}

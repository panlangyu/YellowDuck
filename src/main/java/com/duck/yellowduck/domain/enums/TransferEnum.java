package com.duck.yellowduck.domain.enums;

/**
 * 聊天枚举类，异常提示
 */
public enum TransferEnum {


    TRANSFER_SYSTEM_ERR(1001,"系统异常"),
    TRANSFER_NOT_USER_INFO(1002,"用户不存在"),
    TRANSFER_NOT_TRANSFER_REPEAT(1005,"用户未拥有该币种"),
    TRANSFER_NOT_GT_ZERO(1006,"请输入大于 0 的正数"),
    TRANSFER_AMOUNT_INSUFFICIENT(1007,"币种数量不足"),
    TRANSFER_ABSENTEEISM_REPEAT(1008,"旷工费不足"),
    TRANSFER_NOT_BEI_TRANSFER_INFO(1009,"被转账用户不存在"),
    TRANSFER_NOT_INFO(1010,"用户未拥有ETH钱包"),
    TRANSFER_PASSWD_FAIL(1011,"密码输入不正确"),
    TRANSFER_PASSWD_DAMAGE(1012,"密码数据已损坏");


    private Integer code;               //状态码
    private String message;             //提示信息


    private TransferEnum(Integer code, String message) {
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

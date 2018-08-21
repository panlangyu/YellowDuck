package com.duck.yellowduck.domain.enums;

public enum WalletEnum
{
    WALLET_NOT_NULL_ERROR(Integer.valueOf(1001), "钱包地址不能为空"),
    WALLET_NOT_EXISTENT_ERRORS(Integer.valueOf(1002), "币种不存在"),
    WALLET_INSERT_FALL(Integer.valueOf(1007), "开通钱包失败"),
    WALLET_EXISTENT_COINNAME(Integer.valueOf(1008), "该币种已存在");

    private String message;
    private Integer code;

    private WalletEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Integer getCode()
    {
        return this.code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }
}

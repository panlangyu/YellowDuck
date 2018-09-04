package com.duck.yellowduck.domain.exception;

import com.duck.yellowduck.domain.enums.WalletEnum;

public class WalletException extends RuntimeException {

    private WalletEnum walletEnum;

    public WalletException(WalletEnum walletEnum)
    {
        this.walletEnum = walletEnum;
    }

    public WalletEnum getWalletEnum()
    {
        return this.walletEnum;
    }

    public void setWalletEnum(WalletEnum walletEnum)
    {
        this.walletEnum = walletEnum;
    }
}

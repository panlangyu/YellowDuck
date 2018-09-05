package com.duck.yellowduck.domain.model.vo;


/**
 * 封装币种信息
 */
public class WalletStatusVo {

    private Integer id;                 //钱包编号

    private String coinName;            //币种名称

    private String address;             //地址

    private String contractAddr;        //合约币地址

    //private Boolean status;             //状态


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoinName()
    {
        return this.coinName;
    }

    public void setCoinName(String coinName)
    {
        this.coinName = coinName;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}

package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Investment;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public  interface InvestmentMapper
{
    public  Integer insertUserInvestmentInfo(List<Investment> list)
            throws Exception;

    public  Investment selectInvestmentByWalletId(@Param("walletId") Integer walletId)
            throws Exception;

    public  Integer modifyInvestmentInfo(Investment investment)
            throws Exception;
}

package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Investment;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public  interface InvestmentMapper {

    Integer insertUserInvestmentInfo(List<Investment> list);

    Investment selectInvestmentByWalletId(@Param("walletId") Integer walletId);

    Integer modifyInvestmentInfo(Investment investment);

}

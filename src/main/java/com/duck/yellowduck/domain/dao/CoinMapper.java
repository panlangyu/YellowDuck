package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Coin;
import com.duck.yellowduck.domain.model.vo.CoinVoInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 币种Mapper
 */
@Repository
public interface CoinMapper {


    /** 查询单条币种信息 **/
    Coin selectCoinByAddress(@Param("address")String address);

    /** 查询所有币种信息 **/
    List<CoinVoInfo> selectCoinList();

    /** 按编号查询币种单条信息 **/
    CoinVoInfo selectCoinById(@Param("id")Integer id,@Param("coinName")String coinName);

}

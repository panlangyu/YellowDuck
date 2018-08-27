package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.Coin;
import com.duck.yellowduck.domain.model.vo.CoinVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 币种Mapper
 */
@Repository
public interface CoinMapper {


    /**
     * 查询单条币种信息
     * @param address
     * @return
     * @throws Exception
     */
    public Coin selectCoinByAddress(@Param("address")String address)throws Exception;

    /**
     * 查询所有币种信息
     * @return
     * @throws Exception
     */
    public List<CoinVo> selectCoinList() throws Exception;

}

package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.vo.CoinVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinMapper {

    public List<CoinVo> selectCoinList()
            throws Exception;

}

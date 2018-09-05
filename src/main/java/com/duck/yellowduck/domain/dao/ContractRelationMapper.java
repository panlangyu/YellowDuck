package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.ContractRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 合约币Mapper
 */
@Repository
public interface ContractRelationMapper {

    /**
     * 添加合约币信息
     * @param paramContractRelation
     * @return
     * @throws Exception
     */
    Integer insertContractRelationInfo(ContractRelation paramContractRelation);


    /**
     * 查询合约币信息是否存在
     * @param paramInteger
     * @param paramString
     * @return
     * @throws Exception
     */
    ContractRelation findWalletByUserIdAndAddress(@Param("userId") Integer paramInteger,
                                                  @Param("contractAddr") String paramString) ;



}

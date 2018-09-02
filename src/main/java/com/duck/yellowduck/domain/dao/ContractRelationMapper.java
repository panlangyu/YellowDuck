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
     * 新增合约币信息
     * @param contractRelation
     * @return
     * @throws Exception
     */
    public Integer insertContractRelationInfo(ContractRelation contractRelation)throws Exception;

    /**
     * 查询合约币是否存在
     * @param userId
     * @param contractAddr
     * @return
     * @throws Exception
     */
    public ContractRelation findWalletByUserIdAndAddress(@Param("userId")Integer userId,
                                                         @Param("contractAddr")String contractAddr)throws Exception;




}

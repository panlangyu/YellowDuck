package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.RedEnvelopers;
import com.duck.yellowduck.domain.model.vo.RedEnvelopersVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 红包Mapper
 */
@Repository
public interface RedEnvelopersMapper {

    /**
     * 查询用户发的单个红包
     * @param id  红包编号
     * @param userId 用户编号
     * @return
     * @throws Exception
     */
    RedEnvelopersVo findUserRedEnvelopersInfo(@Param("id")Integer id,
                                                     @Param("userId")Integer userId);


    /**
     * 新增红包信息
     * @param redEnvelopers
     * @return
     * @throws Exception
     */
    Integer insertRedEnvelopersInfo(RedEnvelopers redEnvelopers);


}

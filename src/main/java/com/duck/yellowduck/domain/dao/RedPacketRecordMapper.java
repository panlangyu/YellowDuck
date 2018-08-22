package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.RedPacketRecord;
import com.duck.yellowduck.domain.model.vo.RedPacketRecordVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 个人用户红包Mapper
 */
@Repository
public interface RedPacketRecordMapper {


    /**
     * 用户当前红包记录
     * @param userId
     * @param redEnvelopersId
     * @return
     * @throws Exception
     */
    public List<RedPacketRecordVo> findUserRedPacketRecordInfo(@Param("userId")Integer userId,
                                                               @Param("redEnvelopersId")Integer redEnvelopersId)throws Exception;


    /**
     * 新增红包记录
     * @param redPacketRecord
     * @return
     * @throws Exception
     */
    public Integer insertRedPacketRecordInfo(RedPacketRecord redPacketRecord)throws Exception;


}

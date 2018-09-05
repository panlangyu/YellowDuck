package com.duck.yellowduck.domain.dao;

import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.vo.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户Mapper
 */
@Repository
public interface UserMapper {

    /** 按手机号查询用户是否存在 **/
    UserVo findUserExist(@Param("phone") String phone);

    /** 添加用户信息 **/
    Integer insertUserInfo(User user);

    /** 修改用户信息 **/
    Integer modifyUserInfo(User user);

    /** 按编号查询用户信息 **/
    UserVo findUserById(@Param("id") Integer id);

}

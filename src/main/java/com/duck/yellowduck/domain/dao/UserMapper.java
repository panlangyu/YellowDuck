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


    public  UserVo findUserExist(@Param("phone") String phone)throws Exception;


    public  Integer insertUserInfo(User user)throws Exception;


    public  Integer modifyUserInfo(User user)throws Exception;


    public  UserVo findUserById(@Param("id") Integer id)throws Exception;

}

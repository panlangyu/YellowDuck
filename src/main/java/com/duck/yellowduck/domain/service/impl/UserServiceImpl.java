package com.duck.yellowduck.domain.service.impl;

import com.duck.yellowduck.domain.dao.UserMapper;
import com.duck.yellowduck.domain.enums.UserEnum;
import com.duck.yellowduck.domain.exception.UserException;
import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.UserVo;
import com.duck.yellowduck.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Service
 */
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;                      //用户Mapper


    @Override
    public ApiResponseResult synchronousUserInfo(User user) {

        Integer result = null;

        UserVo userInfo = userMapper.findUserExist(user.getPhone());
        if (null == userInfo) {

            result = userMapper.insertUserInfo(user);

            if (result == 0) {

                throw new UserException(UserEnum.USER_INSERT_FAIL);
            }

            return ApiResponseResult.build(200, "success", "同步数据成功", result);
        }

        result = userMapper.modifyUserInfo(user);
        if (result == 0) {

            throw new UserException(UserEnum.USER_MODIFY_FAIL);
        }

        return ApiResponseResult.build(200, "success", "同步数据成功", result);
    }

    @Override
    public ApiResponseResult queryUserByPhone(String phone) {

        UserVo user = userMapper.findUserExist(phone);

        if (null == user) {

            throw new UserException(UserEnum.USER_NOT_INFO);
        }

        return ApiResponseResult.build(200, "success", "当前用户信息", user);
    }

}

package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;

/**
 * 用户Service
 */
public interface UserService {

    /** 同步用户信息 **/
    ApiResponseResult synchronousUserInfo(User user);

    /** 查询用户信息 **/
    ApiResponseResult queryUserByPhone(String phone);




}

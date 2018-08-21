package com.duck.yellowduck.domain.service;

import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;

public abstract interface UserService
{
    public abstract ApiResponseResult synchronousUserInfo(User paramUser)
            throws Exception;

    public abstract ApiResponseResult queryUserByPhone(String paramString)
            throws Exception;
}

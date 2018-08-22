package com.duck.yellowduck.domain.service.impl;

import com.duck.yellowduck.domain.dao.RedEnvelopersMapper;
import com.duck.yellowduck.domain.dao.UserMapper;
import com.duck.yellowduck.domain.dao.WalletMapper;
import com.duck.yellowduck.domain.service.RedEnvelopesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 红包ServiceImpl
 */
@Service
public class RedEnvelopesServiceImpl implements RedEnvelopesService {

    @Autowired
    private RedEnvelopersMapper redEnvelopesMapper;          //红包Mapper

    @Autowired
    private UserMapper userMapper;                          //用户Mapper

    @Autowired
    private WalletMapper walletMapper;                      //钱包Mapper




}

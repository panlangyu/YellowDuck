package com.duck.yellowduck.domain.service.impl;

import com.duck.yellowduck.domain.dao.TranscationMapper;
import com.duck.yellowduck.domain.dao.UserMapper;
import com.duck.yellowduck.domain.dao.WalletMapper;
import com.duck.yellowduck.domain.model.model.Wallet;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.TranscationVo;
import com.duck.yellowduck.domain.model.vo.UserVo;
import com.duck.yellowduck.domain.service.TranscationService;
import com.duck.yellowduck.publics.CalendarUtil;
import com.duck.yellowduck.publics.PageBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易订单Service
 */
@Service
public class TranscationServiceImpl implements TranscationService {


    @Autowired
    private TranscationMapper transcationMapper;            //交易订单Mapper

    @Autowired
    private UserMapper userMapper;                          //用户信息Mapper

    @Autowired
    private WalletMapper walletMapper;                      //钱包Mapper


    @Override
    public ApiResponseResult selectUserCoinTransactionList(Integer currentPage, Integer currentSize, 
                                                           Integer userId, String coinName) throws Exception {
        
        PageHelper.startPage(currentPage, currentSize);

        List<TranscationVo> voList = this.transcationMapper.selectUserCoinTransactionList(currentPage, currentSize, userId, coinName);
        if (null == voList) {
            return ApiResponseResult.build(2010, "error", "未查询到用户钱包币种交易记录", "");
        }
        PageInfo<TranscationVo> pageInfo = new PageInfo(voList);

        PageBean<TranscationVo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(pageInfo.getTotal());
        pageBean.setItems(voList);

        return ApiResponseResult.build(200, "success", "查询用户钱包币种交易记录", pageBean);
    }

    @Override
    public ApiResponseResult selectUserCoinTransactionListInfo(Integer currentPage, Integer currentSize,
                                                               Integer userId, String startTime) throws Exception {
        String endTime = "";

        if (startTime != null && !startTime.equals("")) {

            String[] str = CalendarUtil.assemblyDate(startTime);
            startTime = str[0];
            endTime = str[1];
        }
        PageHelper.startPage(currentPage, currentSize);

        List<TranscationVo> voList = this.transcationMapper.selectWalletUserCoinTransactionList(currentPage, currentSize, userId, null, startTime, endTime);
        if (null == voList) {
            return ApiResponseResult.build(2010, "error", "未查询到币种交易记录", "");
        }
        PageInfo<TranscationVo> pageInfo = new PageInfo(voList);

        PageBean<TranscationVo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(pageInfo.getTotal());
        pageBean.setItems(voList);

        return ApiResponseResult.build(200, "success", "查询币种交易记录", pageBean);
    }

    @Override
    public ApiResponseResult selectUserCoinTransactionTotal(Integer userId, String startTime) throws Exception {

        Map<String, Object> map = new HashMap();

        String endTime = "";
        if (startTime != null && !startTime.equals("")) {

            String[] str = CalendarUtil.assemblyDate(startTime);
            startTime = str[0];
            endTime = str[1];
        }
        Double toChangeInto = this.transcationMapper.selectUserCoinToChargeInfoTotal(userId, startTime, endTime);

        map.put("toChangeInto", 0);
        if (toChangeInto != null && toChangeInto != 0) {
            map.put("toChangeInto", toChangeInto);
        }

        Double turnTo = this.transcationMapper.selectUserCoinTurnToTotal(userId, startTime, endTime);

        map.put("turnTo", 0);
        if (turnTo != null && turnTo != 0) {
            map.put("turnTo", turnTo);
        }
        List<Map<String, Object>> list = new ArrayList();
        list.add(map);

        return ApiResponseResult.build((200), "success", "查询币种交易记录收入支出总额", list);
    }

    @Override
    public ApiResponseResult selectUserCoinTrunToChargeTotal(Integer userId, String startTime) throws Exception{

        Map<String, Object> map = new HashMap();

        String endTime = "";
        if (startTime != null && !startTime.equals(""))
        {
            String[] str = CalendarUtil.assemblyDate(startTime);
            startTime = str[0];
            endTime = str[1];
        }
        map = this.transcationMapper.selectUserCoinTrunToChargeTotal(userId, startTime, endTime);

        List<Map<String, Object>> list = new ArrayList();
        list.add(map);

        return ApiResponseResult.build(200, "success", "查询币种交易记录收入支出总额", list);
    }

    @Override
    public ApiResponseResult selectWalletUserCoinTransactionList(Integer currentPage, Integer currentSize,
                                                                 Integer userId, String coinName, String startTime) throws Exception {
        PageHelper.startPage(currentPage, currentSize);

        List<TranscationVo> voList = this.transcationMapper.selectWalletUserCoinTransactionList(currentPage, currentSize, userId, coinName, startTime, null);
        if (null == voList) {
            return ApiResponseResult.build(2010, "error", "未查询到钱包管理用户币种交易记录", "");
        }
        PageInfo<TranscationVo> voPageInfo = new PageInfo(voList);

        PageBean<TranscationVo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(voPageInfo.getTotal());
        pageBean.setItems(voList);

        return ApiResponseResult.build(200, "success", "查询钱包管理用户币种交易记录", pageBean);
    }

    @Override
    public ApiResponseResult findUserTransactionList(Integer currentPage, Integer currentSize,
                                                     String phone, String coinName) throws Exception {
        UserVo userInfo = this.userMapper.findUserExist(phone);
        if (null == userInfo) {
            
            return ApiResponseResult.build(2010, "error", "该用户不存在", "");
        }
        Wallet wallet = walletMapper.selectUserWalletETHAddress(userInfo.getId(),0);
        if (null == wallet) {
            
            return ApiResponseResult.build(2010, "error", "该用户下没有该币种", "");
        }
        PageHelper.startPage(currentPage, currentSize);

        List<TranscationVo> voList = this.transcationMapper.selectUserCoinTransactionList(currentPage, currentSize, userInfo.getId(), coinName);

        if (null == voList) {
            return ApiResponseResult.build(2010, "error", "未查询到钱包用户币种交易记录", "");
        }
        PageInfo<TranscationVo> voPageInfo = new PageInfo(voList);

        PageBean<TranscationVo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(voPageInfo.getTotal());
        pageBean.setItems(voList);

        return ApiResponseResult.build(200, "success", "钱包管理用户币种交易记录", pageBean);
    }


}

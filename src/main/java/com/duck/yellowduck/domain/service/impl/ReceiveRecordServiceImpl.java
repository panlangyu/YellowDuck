package com.duck.yellowduck.domain.service.impl;

import com.duck.yellowduck.domain.dao.ReceiveRecordMapper;
import com.duck.yellowduck.domain.enums.ReceiveRecordEnum;
import com.duck.yellowduck.domain.exception.ReceiveRecordException;
import com.duck.yellowduck.domain.model.model.ReceiveRecord;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.service.ReceiveRecordService;
import com.duck.yellowduck.publics.PageBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 用户领取记录ServiceImpl
 */
@Service
public class ReceiveRecordServiceImpl implements ReceiveRecordService {


    @Autowired
    private ReceiveRecordMapper receiveRecordMapper;                //领取记录Mapper


    @Override
    public ApiResponseResult findReceiveRecordListInfo(Integer currentPage,Integer currentSize,String address) {


        PageHelper.startPage(currentPage,currentSize);

        List<ReceiveRecord> voList = receiveRecordMapper.selectReceiveRecordListInfo(currentPage,currentSize,address);
        if(voList.isEmpty()){

            throw new ReceiveRecordException(ReceiveRecordEnum.RECEIVE_NOT_INFO);
        }

        PageInfo<ReceiveRecord> pageInfo = new PageInfo(voList);
        PageBean<ReceiveRecord> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(pageInfo.getTotal());
        pageBean.setItems(voList);

        return ApiResponseResult.build(200,"success","查询用户领取记录",pageBean);
    }

    @Override
    public ApiResponseResult findReceiveIsTrue(String phone) {

        Boolean flag = true;

        Integer result = receiveRecordMapper.selectReceiveIsTrue(phone);

        if(result == 0){

            flag = false;
        }

        return ApiResponseResult.build(200,"success","查询用户是否领取福利",flag);
    }


}

package com.duck.yellowduck.domain.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duck.yellowduck.domain.dao.TransferMapper;
import com.duck.yellowduck.domain.dao.UserMapper;
import com.duck.yellowduck.domain.dao.WalletMapper;
import com.duck.yellowduck.domain.model.model.Transcation;
import com.duck.yellowduck.domain.model.model.Transfer;
import com.duck.yellowduck.domain.model.model.Wallet;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.TransferVo;
import com.duck.yellowduck.domain.model.vo.UserVo;
import com.duck.yellowduck.domain.model.vo.WalletUtilsVo;
import com.duck.yellowduck.domain.model.vo.WalletVXUtilsVo;
import com.duck.yellowduck.domain.service.TransferService;
import com.duck.yellowduck.domain.service.WalletService;
import com.duck.yellowduck.publics.CalendarUtil;
import com.duck.yellowduck.publics.HttpUtils;
import com.duck.yellowduck.publics.ObjectUtils;
import com.duck.yellowduck.publics.PageBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户转账ServiceImpl
 */
@Service
public class TransferServiceImpl implements TransferService {


    @Autowired
    private TransferMapper transferMapper;              //转账Mapper

    @Autowired
    private WalletMapper walletMapper;                  //钱包Mapper

    @Autowired
    private UserMapper userMapper;                      //用户Mapper

    @Autowired
    private WalletService walletService;                //钱包Service

    @Value("${yellowduck.url}")
    private String url ;                                //第三方服务器端口



    @Transactional
    @Override
    public ApiResponseResult chatAndTransfer(WalletVXUtilsVo wallet) throws Exception {

        ApiResponseResult apiResponse = new ApiResponseResult();

        //查询当前用户是否存在
        UserVo user = userMapper.findUserExist(wallet.getPhone());
        if (null == user) {

            return ApiResponseResult.build(2013, "error", "当前用户不存在", "");
        }

        //查询当前用户是否有钱包信息
        Wallet userWallet = walletMapper.selectUserWalletByCoinId(user.getId(), wallet.getCoinName());
        if (null == userWallet) {

            return ApiResponseResult.build(2011, "error", "用户未拥有该币种", "");
        }

        //验证密码输入是否正确
        if(userWallet.getPasswd() != null && !userWallet.getPasswd().equals("")){

            if( !userWallet.getPasswd().equals(wallet.getPasswd())){

                return ApiResponseResult.build(2011, "error", "密码输入不正确", "");
            }
        }

        //查询被转账用户是否存在
        UserVo earnerUser = userMapper.findUserExist(wallet.getEarnerPhone());
        if (null == earnerUser) {
            return ApiResponseResult.build(2013, "error", "被转账用户不存在", "");
        }


        String address = "";            //被转账地址
        //查询被当前用户是否有钱包信息
        Wallet earnerWallet = walletMapper.selectUserWalletByCoinId(earnerUser.getId(), wallet.getCoinName());
        if (null == earnerWallet) {

            //添加合约币信息
            apiResponse = walletService.queryContractAddr(wallet.getEarnerPhone(),userWallet.getContractAddr());

        }

        //判断用户是否拥有该币种
        if(apiResponse != null && apiResponse.getCode() == 0){

            address = earnerWallet.getAddress();                //被抓账用户有该币种地址时
        }else{

            address = apiResponse.getData().toString();         //被转账地址
        }

        //锁钱包表
        walletMapper.lockWalletTable();

        //判断金额是否 大于 0
        int trun = new BigDecimal(wallet.getValue()).compareTo(BigDecimal.ZERO);
        if ((trun == 0) || (trun == -1)) {
            return ApiResponseResult.build(2010, "error", "请输入大于 0 的正数", "");
        }

        String uri = "";            //接收第三方URL

        Map<String, String> amountMap = new HashMap();
        Map<String, String> txMap = new HashMap();

        if (userWallet.getContractAddr() != null && (!userWallet.getContractAddr().equals(""))) {

            uri = url + "/token/balance";

            amountMap.put("from", userWallet.getAddress());
            amountMap.put("contractAddr", userWallet.getContractAddr());

        } else {

            uri = url + "/address";
            amountMap.put("address", userWallet.getAddress());
        }

        String str = "";
        BigDecimal price = new BigDecimal("0");                 //拿到用户的币种数量(金额)

        str = HttpUtils.sendGet(uri, amountMap, 2);            //查询用户币种数量

        if(str == null || str.equals("")){

            return ApiResponseResult.build(2010, "error", "余额不足", "");
        }

        price = ObjectUtils.getPrice(str);                          //拿出币种数量

        //看是否出异常
        Integer compareZero = price.compareTo(BigDecimal.ZERO);
        if(compareZero == -1){

            return ApiResponseResult.build(2011, "error", "出现异常", "");
       }

        //拿出金额做比较
        int compare = price.compareTo(new BigDecimal(wallet.getValue()));
        if (compare == 0 || compare == -1) {
            return ApiResponseResult.build(2011, "error", "币种数量不足", "");
        }

        txMap.put("sign", userWallet.getPasswd());
        txMap.put("to", address);
        txMap.put("value", wallet.getValue());
        //txMap.put("gasPrice", "7810");
        //txMap.put("gas", "6050");

        if (userWallet.getContractAddr() != null && !userWallet.getContractAddr().equals("")) {

            uri = url + "/token/sendTx";

            txMap.put("from", userWallet.getAddress());
            txMap.put("contractAddr", userWallet.getContractAddr());
        } else {

            uri = url + "/sendTx";
            txMap.put("from", userWallet.getAddress());
        }

        String json = JSONArray.toJSONString(txMap);

        str = HttpUtils.sendPost(uri, json);

        if (str == null || str.equals("")) {

            return ApiResponseResult.build(2010, "error", "提币失败", "");
        }

        String hash = ObjectUtils.getHash(str);         //拿出成功的hash

        if(hash != null && hash.equals("-1")){

            return ApiResponseResult.build(2011, "error", "旷工费不足", "");
        }
        if(hash == null || hash.equals("")){

            return ApiResponseResult.build(2011, "error", "出现异常", "");
        }

        wallet.setHash(hash);                           //转账成功的hash值

        //新增转出记录
        wallet.setUserId(user.getId());                 //用户编号
        Integer num = insertTransferTurnTo(wallet);

        //新增转入记录
        wallet.setUserId(earnerUser.getId());           //被转账者编号
        num = insertTransferToCharge(wallet);

        return ApiResponseResult.build(200, "success", "转账成功", num);
    }

    @Override
    public ApiResponseResult findUserTransferInfo(Integer currentPage, Integer currentSize,
                                                  String phone, String startTime) throws Exception {

        //查询当前用户是否存在
        UserVo user = userMapper.findUserExist(phone);
        if (null == user) {
            return ApiResponseResult.build(2013, "error", "该用户不存在", "");
        }

        String endTime = "";
        if (startTime != null && !startTime.equals("")){

            String[] str = CalendarUtil.assemblyDate(startTime);
            startTime = str[0];
            endTime = str[1];
        }

        PageHelper.startPage(currentPage, currentSize);

        List<TransferVo> voList = transferMapper.findUserTransferInfo(currentPage,currentSize,user.getId(),startTime,endTime);

        if (null == voList) {
            return ApiResponseResult.build(2011, "error", "未查询到用户转账信息", "");
        }

        PageInfo<TransferVo> pageInfo = new PageInfo(voList);
        PageBean<TransferVo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(pageInfo.getTotal());
        pageBean.setItems(voList);

        return ApiResponseResult.build(200, "success", "用户转账信息", pageBean);
    }

    /**
     * 用户聊天转账 (转出)
     * @param wallet
     * @return
     * @throws Exception
     */
    public Integer insertTransferTurnTo(WalletVXUtilsVo wallet) throws Exception {


        Transfer transfer = new Transfer();
        transfer.setUserId(wallet.getUserId());                 //用户编号
        transfer.setCoinName(wallet.getCoinName());             //币种名称
        transfer.setStatus(2);                                  //转账方式 1、转入 2、转出
        transfer.setTransferStatus(1);                          //状态 1、已领取 2、未领取 3、已过期
        transfer.setHash(wallet.getHash());                     //转账成功的hash值
        transfer.setAmount(new BigDecimal(wallet.getValue()));  //金额
        transfer.setRemark(ObjectUtils.getWalletRemark(wallet.getRemark(),transfer.getStatus()));  //备注

        Integer num = transferMapper.insertTransferTurnTo(transfer);
        transfer.setId(transfer.getId());                       //获取自增后的编号

        return num;
    }


    /**
     * 用户聊天转账 (转入)
     * @param wallet
     * @return
     * @throws Exception
     */
    public Integer insertTransferToCharge(WalletVXUtilsVo wallet) throws Exception {


        Transfer transfer = new Transfer();
        transfer.setUserId(wallet.getUserId());                 //用户编号
        transfer.setStatus(1);                                  //转账方式 1、转入 2、转出
        transfer.setTransferStatus(1);                          //状态 1、已领取 2、未领取 3、已过期
        transfer.setHash(wallet.getHash());                     //转账成功的hash值
        transfer.setCoinName(wallet.getCoinName());             //币种名称
        transfer.setAmount(new BigDecimal(wallet.getValue()));  //金额
        transfer.setRemark(ObjectUtils.getWalletRemark(wallet.getRemark(),transfer.getStatus()));  //备注

        Integer num = transferMapper.insertTransferToCharge(transfer);
        transfer.setId(transfer.getId());                       //获取自增后的编号

        return num;
    }


    public static void main(String[] args) {

        String str ="{\"body\":{\"balance\":\"887\"},\"code\":4,\"type\":\"ok\"}";

        JSONObject jsonObject = JSONObject.parseObject(str);

        System.out.println(jsonObject.get("body"));

        JSONObject bof = (JSONObject)jsonObject.get("body");

        System.out.println(bof.get("balance"));



    }


}

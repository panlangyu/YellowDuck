package com.duck.yellowduck.domain.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.duck.yellowduck.domain.dao.*;
import com.duck.yellowduck.domain.enums.WalletEnum;
import com.duck.yellowduck.domain.exception.WalletException;
import com.duck.yellowduck.domain.model.model.*;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.*;
import com.duck.yellowduck.domain.service.WalletService;
import com.duck.yellowduck.publics.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

/**
 * 钱包业务ServiceImpl
 */
@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletMapper walletMapper;                         //钱包Mapper

    @Autowired
    private ContractRelationMapper contractRelationMapper;     //合约币Mapper

    @Autowired
    private TranscationMapper transcationMapper;               //钱包交易记录Mapper

    @Autowired
    private UserMapper userMapper;                             //用户Mapper

    @Autowired
    private CoinMapper coinMapper;                             //币种Mapper

    @Value("${yellowduck.url}")
    private String url ;                                       //第三方服务器端口

    private static String pubkey;                              //RSA加密公链

    private static String prikey;                              //RSA加密私链

    static{
         pubkey = RewardConfigureUtils.getInstance().getPublicKey();          //获取公链
         prikey = RewardConfigureUtils.getInstance().getPrivateKey();         //获取私有链
    }


    @Override
    public ApiResponseResult createWalletInfo(User user) {

        UserVo userInfo = userMapper.findUserExist(user.getPhone());
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
        }

        String addressExist = walletMapper.findWalletAddressByUserId(userInfo.getId(), "ETH");
        if (addressExist != null && !addressExist.equals("")) {

            throw new WalletException(WalletEnum.WALLET_REPEAT_INFO);
        }

        String uri = "";                //第三方API接口路径
        uri = url + "/register";

        Wallet wallet = new Wallet();
        wallet.setPasswd(user.getPasswd());
        wallet.setUserId(userInfo.getId());

        Map<String, String> map = new HashMap();
        map.put("phone", user.getPhone());
        map.put("txpw", wallet.getPasswd());

        String json = JSONArray.toJSONString(map);

        String str = HttpUtils.sendPost(uri, json);
        if (str == null || str.equals("") || str.equals("null")) {

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
        }

        String address = ObjectUtils.getAddress(str);

        if(address == null || address.equals("")){

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
        }

        Coin coin = coinMapper.selectCoinByAddress("☺");
        if (null == coin) {

            throw new WalletException(WalletEnum.WALLET_NOT_EXISTENT_ERROR);
        }

        wallet.setAddress(address);
        wallet.setCoinId(coin.getId());
        wallet.setCoinName("ETH");
        wallet.setPrivateKey(ObjectUtils.getUUID());
        wallet.setKeystore(ObjectUtils.getUUID());
        wallet.setCoinImg("http://airdrop.atmall.org:8761/image/ic_eth_coin_logo.png");
        //wallet.setCoinImg("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1701349413,5323685&fm=173&app=25&f=JPEG?w=400&h=240&s=219B1DDD6A2255074C38E0700300D07A");

        //新增钱包信息
        Integer num = walletMapper.insertWalletInfo(wallet);
        if (num == 0) {

            throw new WalletException(WalletEnum.WALLET_NOT_OPEN_UP);
        }

        return ApiResponseResult.build(200, "success", "开通钱包成功", str);
    }

    @Override
    public ApiResponseResult findUserWalletList(Integer currentPage,Integer currentSize,String phone,Integer id,String coinName) {

        UserVo userInfo = userMapper.findUserExist(phone);
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
        }

        PageHelper.startPage(currentPage, currentSize);

        List<WalletListInfo> walletVoList = new ArrayList();

        List<WalletVo> voList = null;                       //用户币种列表,单条信息

        // 查询用户列表数据
        if(coinName == null || coinName.equals("")){

            voList = walletMapper.selectUserWalletInfo(currentPage,currentSize,userInfo.getId(), coinName);
        }else if(coinName != null && !coinName.equals("") && coinName.equals("ETH") ){

            voList = walletMapper.selectETHCoinInfoById(currentPage,currentSize,userInfo.getId(), id);
        }else{

            voList = walletMapper.selectContractCoinInfoById(currentPage,currentSize,userInfo.getId(), id);
        }

        if (null == voList) {

            throw new WalletException(WalletEnum.WALLET_NOT_LIST_INFO);
        }

        Map<String, String> map = new HashMap();

        WalletListInfo walletVo = null;

        String uri = "";                //第三方API接口路径

        String str = "";                //接收第三方返回的数据

        String address = "";            //ETH地址

        Coin coin = new Coin();         //币种对象

        for (WalletVo vo : voList) {

            map = new HashMap();

            walletVo = new WalletListInfo();
            if (!vo.getCoinName().equals("ETH")) {

                uri = url + "/token/balance";

                address = walletMapper.findWalletAddressByUserId(vo.getUserId(), "ETH");
                map.put("contractAddr", vo.getContractAddr());
                map.put("from", address);

                coin = coinMapper.selectCoinByAddress(vo.getContractAddr());
                if(null == coin){

                    walletVo.setWalletTotal(new BigDecimal("0"));         //如果没有币种价格信息,就默认为0
                }else{

                    walletVo.setWalletTotal(coin.getMarketPrice());            //市场价格
                }

            }else {

                uri = url + "/address";

                map.put("address", vo.getAddress());
                address = vo.getAddress();

                coin = coinMapper.selectCoinByAddress("☺");             //特殊符号查询ETH
                if(null == coin){

                    walletVo.setWalletTotal(new BigDecimal("0"));         //如果没有币种价格信息,就默认为0
                }else{

                    walletVo.setWalletTotal(coin.getMarketPrice());            //市场价格
                }

            }
            BigDecimal price = new BigDecimal("0");

            str = HttpUtils.sendGet(uri, map, (2));
            if (str == null || str.equals("") || str.equals("null")) {

                throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            }

            price = ObjectUtils.getPrice(str);

            //是否能拿到币种金额
            Integer compareZero = price.compareTo(BigDecimal.ZERO);
            if(compareZero == -1){

                throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            }

            walletVo.setId(vo.getId());
            walletVo.setAddress(address);
            walletVo.setCoinName(vo.getCoinName());

            walletVo.setAmount(price);
            walletVo.setCoinImg(vo.getCoinImg());
            walletVo.setContractAddr(vo.getContractAddr());

            walletVoList.add(walletVo);
        }

        PageInfo<WalletListInfo> pageInfo = new PageInfo(walletVoList);
        PageBean<WalletListInfo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(pageInfo.getTotal());
        pageBean.setItems(walletVoList);

        return ApiResponseResult.build(200, "success", "查询用户钱包币种列表数据", pageBean);
    }

    @Transactional
    @Override
    public ApiResponseResult modifyWithdrawMoney(WalletUtilsVo wallet) {

        //查看当前用户是否存在
        UserVo userInfo = userMapper.findUserExist(wallet.getPhone());
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
        }

        Wallet userWalletCoin = null;

        if(wallet.getCoinName() != null && wallet.getCoinName().equals("ETH")){

            userWalletCoin = walletMapper.selectUserWalletETHAddress(userInfo.getId(), wallet.getId());
        }else {

            userWalletCoin = walletMapper.selectUserWalletContractAddrInfo(userInfo.getId(), wallet.getId());
        }

        //Wallet userWalletCoin = walletMapper.selectUserWalletById(userInfo.getId(), wallet.getId());
        if (userWalletCoin == null) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_REPEAT);
        }

           /*  if(userWalletCoin.getAddress().equals(walletCoin.getAddress())){

            return ApiResponseResult.build(2010,"error","不允许同一个地址发生交易","");
        }

        if(userWalletCoin.getCoinId() != walletCoin.getCoinId()){

            return ApiResponseResult.build(2010,"error","不能跨币种转账,只能同币交易","");
        }*/

        //当前用户转账锁钱包表
        walletMapper.lockWalletTable(userWalletCoin.getId());

        int trun = new BigDecimal(wallet.getValue()).compareTo(BigDecimal.ZERO);
        if (trun == 0 || trun == -1) {

            throw new WalletException(WalletEnum.WALLET_NOT_GT_ZERO);
        }

        String uri = "";                //第三方API接口路径

        Map<String, String> amountMap = new HashMap();
        Map<String, String> txMap = new HashMap();

        if (userWalletCoin.getContractAddr() != null && !userWalletCoin.getContractAddr().equals("")) {

            uri = url+ "/token/balance";
            amountMap.put("contractAddr", userWalletCoin.getContractAddr());
            amountMap.put("from", userWalletCoin.getAddress());
        } else {

            uri = url + "/address";
            amountMap.put("address", userWalletCoin.getAddress());
        }
        String str = "";
        BigDecimal price = new BigDecimal("0");

        str = HttpUtils.sendGet(uri, amountMap, 2);

        if(str == null || str.equals("") || str.equals("null")){

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
        }

        price = ObjectUtils.getPrice(str);
        //看是否出异常

        Integer compareZero = price.compareTo(BigDecimal.ZERO);
        if(compareZero == -1){

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
        }

        //拿出币种数量和转账数量比较
        int compare = price.compareTo(new BigDecimal(wallet.getValue()));
        if (compare == 0 || compare == -1) {

            throw new WalletException(WalletEnum.WALLET_AMOUNT_INSUFFICIENT);
        }

        txMap.put("sign", userWalletCoin.getPasswd());
        txMap.put("to", wallet.getAddress());
        txMap.put("value", wallet.getValue());
        //txMap.put("gas", "6050");
        //txMap.put("gasPrice", "7810");
        if (userWalletCoin.getContractAddr() != null && !userWalletCoin.getContractAddr().equals("")) {

            uri = url + "/token/sendTx";

            txMap.put("contractAddr", userWalletCoin.getContractAddr());
            txMap.put("from", userWalletCoin.getAddress());
        }else {

            uri = url + "/sendTx";
            txMap.put("from", userWalletCoin.getAddress());
        }

        String json = JSONArray.toJSONString(txMap);
        str = HttpUtils.sendPost(uri, json);

        if (str == null || str.equals("") || str.equals("null")) {

            throw new WalletException(WalletEnum.WALLET_CURRENCY_FAILURE);
        }

        String hash = ObjectUtils.getHash(str);         //拿出成功的hash

        if(hash != null && hash.equals("-1")){

            throw new WalletException(WalletEnum.WALLET_ABSENTEEISM_REPEAT);
        }
        if(hash == null || hash.equals("")){

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
        }

        wallet.setHash(hash);                           //转账成功的hash值

        //新增转账记录
        Integer num = insertWalletTurnTo(wallet, userWalletCoin);
        if (num == 0) {

            throw new WalletException(WalletEnum.WALLET_INSERT_COINNAME);
        }
        return ApiResponseResult.build(200, "success", "提币成功", num);
    }

    @Override
    public ApiResponseResult queryContractAddr(String phone, String contractAddr) {

        UserVo userInfo = userMapper.findUserExist(phone);
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
        }

        ContractRelation relation = contractRelationMapper.findWalletByUserIdAndAddress(userInfo.getId(), contractAddr);
        if (relation != null) {

            throw new WalletException(WalletEnum.WALLET_REPEAT_INFO);
        }

        List<Wallet> walletList = walletMapper.findUserWalletInfo(userInfo.getId());
        if (null == walletList && walletList.size() == 0) {

            throw new WalletException(WalletEnum.WALLET_NOT_LIST_INFO);
        }

        Wallet walletVo = walletList.get(0);

        String uri = "";                //第三方API接口路径

        uri = url + "/token";

        Map<String, String> map = new HashMap();
        map.put("contractAddr", contractAddr);

        String str = HttpUtils.sendGet(uri, map, 2);
        if (str == null || str.equals("") || str.equals("null")) {

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
        }

        String coinName = ObjectUtils.getNum(str);

        if (coinName.equals("-1")) {

            throw new WalletException(WalletEnum.WALLET_NOT_EXISTENT_ERROR);
        }
        if (coinName.equals("-2")) {

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
        }

        ContractRelation contractRelation = new ContractRelation();

        Coin coin = coinMapper.selectCoinByAddress(contractAddr);
        if (null == coin) {

            throw new WalletException(WalletEnum.WALLET_NOT_EXISTENT_ERROR);
        }
        contractRelation.setCoinId(coin.getId());

        contractRelation.setUserId(userInfo.getId());
        contractRelation.setPrivateKey(ObjectUtils.getUUID());
        contractRelation.setKeystore(ObjectUtils.getUUID());
        contractRelation.setCoinImg("http://airdrop.atmall.org:8761/image/ic_bnl_coin_logo.png");
        contractRelation.setPasswd(walletVo.getPasswd());
        contractRelation.setAddress(walletVo.getAddress());
        contractRelation.setContractAddr(contractAddr);

        /*Wallet wallet = new Wallet();
        if (mapOne.get("type").toString().equals("ok")) {

            for (String string : mapOne.keySet()) {

                if (string.equals("body")) {
                    mapTwo = (Map)mapOne.get("body");
                    wallet.setCoinName(mapTwo.get("symbol").toString());
                }

            }
            wallet.setUserId(userInfo.getId());
            wallet.setPrivateKey(ObjectUtils.getUUID());
            wallet.setKeystore(ObjectUtils.getUUID());
            wallet.setCoinImg("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1701349413,5323685&fm=173&app=25&f=JPEG?w=400&h=240&s=219B1DDD6A2255074C38E0700300D07A");
            wallet.setPasswd(walletVo.getPasswd());
            wallet.setAddress(walletVo.getAddress());
            wallet.setContractAddr(contractAddr);
        }*/

        //新增合约币信息
        Integer num = contractRelationMapper.insertContractRelationInfo(contractRelation);
        if (num == 0) {

            throw new WalletException(WalletEnum.WALLET_INSERT_COINNAME);
        }

        return ApiResponseResult.build(200, "success", "添加成功", contractRelation);
    }

    @Override
    public ApiResponseResult queryAccountList(){

        String uri = "";                //第三方API接口路径
        uri = url + "/accounts";

        Map<String, String> map = new HashMap();
        String str = HttpUtils.sendGet(uri, map, 2);
        if ((str == null) || (str.equals(""))) {

            throw new WalletException(WalletEnum.WALLET_NOT_ACCOUNT_INFO);
        }

        return ApiResponseResult.build(200, "success", "查询所有钱包账户", str);
    }

    @Override
    public ApiResponseResult blockNumber(){

        String uri = "";                //第三方API接口路径
        uri = url + "/blockNumber";

        Map<String, String> map = new HashMap();

        String str = HttpUtils.sendGet(uri, map, (2));
        if (str == null || str.equals("") || str.equals("null")) {

            throw new WalletException(WalletEnum.WALLET_NOT_BLOCK_INFO);
        }

        return ApiResponseResult.build(200, "success", "查询阻塞数信息", JSONArray.parseObject(str));
    }


    @Override
    public ApiResponseResult findWalletListInfo(String phone) {

        UserVo user = userMapper.findUserExist(phone);
        if (null == user) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
        }

        List<WalletStatusVo> voList = walletMapper.findWalletListInfo(user.getId());
        if (null == voList || voList.size() == 0) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_REPEAT);
        }

        return ApiResponseResult.build(200, "success", "用户币种列表", voList);
    }

    @Override
    public ApiResponseResult findWalletAddressByUserId(String phone) {

        //查询当前用户是否存在
        UserVo user = userMapper.findUserExist(phone);

        if(null == user){

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
        }

        String address = walletMapper.findWalletAddressByUserId(user.getId(),"ETH");
        if(address == null || address.equals("")){

            throw new WalletException(WalletEnum.WALLET_NOT_ETH_INFO);
        }

        return ApiResponseResult.build(200,"success","用户ETH钱包地址",address);
    }

    @Override
    public ApiResponseResult rsaShow(String passwd) {

        passwd = ObjectUtils.rsaDecrypt(passwd);           //解密密码

        if(passwd == null || passwd.equals("")){

            throw new WalletException(WalletEnum.WALLET_PASSWD_DAMAGE);
        }

        //String pubkey = RewardConfigureUtils.getInstance().getPublicKey();          //获取公链
        //String prikey = RewardConfigureUtils.getInstance().getPrivateKey();         //获取私有链
        /*String s = "";          //加密后的密码
        try {
            System.out.println("公链："+pubkey);
            System.out.println("私链："+prikey);
            RSAPublicKey publicKey = RSAEncrypt.loadPublicKey(pubkey);
            RSAPrivateKey privateKey = RSAEncrypt.loadPrivateKey(prikey);
            String str = passwd;
            s = RSAEncrypt.encrypt(publicKey, str.getBytes());
            System.out.println("加密后：" + s);
            String s1 = RSAEncrypt.decrypt(privateKey, RSAEncrypt.strToBase64(s));
            System.out.println(s1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

        return ApiResponseResult.build(200,"success","资金密码加密",passwd);
    }



    public Integer insertWalletTurnTo(WalletUtilsVo wallet, Wallet userWalletCoin) {

        Transcation transcation = new Transcation();
        transcation.setUserId(userWalletCoin.getUserId());
        transcation.setCoinName(userWalletCoin.getCoinName());
        transcation.setAmount(new BigDecimal(wallet.getValue()));
        transcation.setTxType(2);
        transcation.setFrom(userWalletCoin.getAddress());
        if ((userWalletCoin.getContractAddr() != null) && (!userWalletCoin.getContractAddr().equals(""))) {
            transcation.setContractAddr(userWalletCoin.getContractAddr());
        }
        transcation.setTo(wallet.getAddress());
        transcation.setHash(wallet.getHash());
        transcation.setTxStatus(1);
        transcation.setRemark(ObjectUtils.getWalletRemark(wallet.getRemark(), transcation.getTxType()));

        Integer num = transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }


}

package com.duck.yellowduck.domain.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.duck.yellowduck.domain.dao.*;
import com.duck.yellowduck.domain.enums.WalletEnum;
import com.duck.yellowduck.domain.exception.WalletException;
import com.duck.yellowduck.domain.model.model.*;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.*;
import com.duck.yellowduck.domain.service.WalletService;
import com.duck.yellowduck.publics.HttpUtils;
import com.duck.yellowduck.publics.ObjectUtils;
import com.duck.yellowduck.publics.PageBean;
import com.duck.yellowduck.publics.RewardConfigureUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
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

    @Autowired
    private DictionaryMapper dictionaryMapper;                 //数据字典Mapper

    @Autowired
    private DirectPrizeMapper directPrizeMapper;               //投资Mapper

    @Autowired
    private InvestmentMapper investmentMapper;                 //利息Mapper

    @Value("${yellowduck.url}")
    private String url ;                                       //第三方服务器端口


    @Override
    public ApiResponseResult selectUserWalletTotal(Integer userId) throws Exception {

        UserVo user = userMapper.findUserById(userId);
        if (null == user) {
            return ApiResponseResult.build(2015, "error", "该用户不存在", "");
        }

        BigDecimal total = walletMapper.selectUserWalletTotal(userId);
        if (null == total) {
            total = new BigDecimal(0);
        }

        return ApiResponseResult.build(200, "success", "查询用户钱包币种总额", total);
    }

    @Override
    public ApiResponseResult selectUserWalletCoinList(Integer currentPage, Integer currentSize,
                                                      Integer userId, String coinName) throws Exception {

        PageHelper.startPage(currentPage, currentSize);

        List<WalletVo> voList = walletMapper.selectUserWalletInfo(currentPage,currentSize,userId, coinName);
        if (null == voList) {
            return ApiResponseResult.build(2011, "error", "未查询到用户钱包币种列表数据", "");
        }

        PageInfo<WalletVo> pageInfo = new PageInfo(voList);
        PageBean<WalletVo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(pageInfo.getTotal());
        pageBean.setItems(voList);

        return ApiResponseResult.build(200, "success", "查询用户钱包币种列表数据", pageBean);
    }

    @Override
    public ApiResponseResult modifyWalletTurnOut(Wallet wallet) throws Exception {

        //锁表,行级锁
        //walletMapper.lockWalletTable();

        Wallet userWalletCoin = new Wallet();
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到用户钱包下的币种信息", "");
        }
        Wallet walletCoin = new Wallet();
        if (walletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到该地址信息", "");
        }
        if (userWalletCoin.getAddress().equals(walletCoin.getAddress())) {
            return ApiResponseResult.build(2010, "error", "不允许同一个地址发生交易", "");
        }
        if (userWalletCoin.getCoinName() != walletCoin.getCoinName()) {
            return ApiResponseResult.build(2010, "error", "不能跨币种转账,只能同币种交易", "");
        }
        List<DictionaryVo> voList = dictionaryMapper.selectDictionaryListById(5, "DEDUCT_FORMALITIES");
        if (null == voList) {
            return ApiResponseResult.build(2010, "error", "未查询到数据字典内容", "");
        }
        DictionaryVo dictionaryVo = (DictionaryVo)voList.get(0);
        BigDecimal deductionPrice = userWalletCoin.getAvailableAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        userWalletCoin.setAvailableAmount(userWalletCoin.getAvailableAmount().subtract(deductionPrice));

        int compare = userWalletCoin.getAvailableAmount().compareTo(wallet.getAmount());
        if ((compare == 0) || (compare == -1)) {
            return ApiResponseResult.build(2011, "error", "币种数量不足", "");
        }

        Integer num = walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "转出失败", "");
        }

        wallet.setId(walletCoin.getId());
        num = walletMapper.modifyWalletToChangeInto(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "转入失败", "");
        }
        num = insertWalletToChangeInto(wallet, walletCoin, userWalletCoin);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "新增失败", "");
        }
        deductionPrice = wallet.getAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        num = insertWalletDeductionTurnTo(wallet, userWalletCoin, deductionPrice);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用记录失败", "");
        }
        wallet.setId(userWalletCoin.getId());
        wallet.setAmount(deductionPrice);
        num = walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "提币聪明", num);
    }

    @Override
    public ApiResponseResult selectUserWalletCoinStraightOrInterest(Integer currentPage, Integer currentSize,
                                                                    Integer userId) throws Exception {

        PageHelper.startPage(currentPage, currentSize);

        Map<String, Object> map = new HashMap();

        List<WalletVo> voList = walletMapper.selectUserWalletInfo(currentPage, currentSize,userId, null);
        if (null == voList) {
            return ApiResponseResult.build(2013, "error", "未查询到管理币种列表信息直推和利息", "");
        }
        PageInfo<WalletVo> pageInfo = new PageInfo(voList);

        List<WalletCoinVo> walletCoinVoList = new ArrayList();

        WalletCoinVo walletCoinVo = null;
        for (WalletVo walletVo : voList)
        {
            walletCoinVo = new WalletCoinVo();

            map = transcationMapper.selectUserWalletCoinStraightOrInterest(userId, walletVo.getCoinName());

            walletCoinVo.setAddress(walletVo.getAddress());
            walletCoinVo.setId(walletVo.getId());

            walletCoinVo.setCoinImg(walletVo.getCoinImg());
            walletCoinVo.setCoinName(walletVo.getCoinName());
            walletCoinVo.setAmount(walletVo.getAmount());
            if (map != null) {
                if ((map.get("straightPush") != null) && (map.get("interest") == null)) {

                }
            }
            walletCoinVoList.add(walletCoinVo);
        }
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(pageInfo.getTotal());
        pageBean.setItems(walletCoinVoList);

        return ApiResponseResult.build(200, "success", "钱包管理币种列表信息直推和利息", pageBean);
    }

    @Override
    public ApiResponseResult selectYesterdayProfit(Integer userId, Integer coinId) throws Exception {

        Map<String, Object> map = new HashMap();

        Wallet wallet = walletMapper.selectUserWalletETHAddress(userId, 0);
        if (null == wallet) {
            return ApiResponseResult.build(2013, "error", "未查询到有该币种信息", "");
        }
        map = walletMapper.selectYesterdayProfit(userId, coinId);

        List<Map<String, Object>> list = new ArrayList();
        list.add(map);

        return ApiResponseResult.build(200, "success", "管理钱包用户币种昨日收益", list);
    }

    @Override
    public ApiResponseResult modifyWalletDepositToChangeInto(Wallet wallet)
            throws Exception {

        //walletMapper.lockWalletTable();

        Wallet userWalletCoin = new Wallet();
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到用户钱包下的币种", "");
        }
        List<DictionaryVo> voList = dictionaryMapper.selectDictionaryListById(2, "DEDUCT_FORMALITIES");
        if (null == voList) {
            return ApiResponseResult.build(2010, "error", "未查询到数据字典内容信息", "");
        }

        DictionaryVo dictionaryVo = (DictionaryVo)voList.get(0);
        BigDecimal deductionPrice = userWalletCoin.getAvailableAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        userWalletCoin.setAvailableAmount(userWalletCoin.getAvailableAmount().subtract(deductionPrice));

        int compare = userWalletCoin.getAvailableAmount().compareTo(wallet.getAmount());
        if ((compare == 0) || (compare == -1)) {
            return ApiResponseResult.build(2012, "error", "币种数量不足", "");
        }
        Integer num = walletMapper.modifyWalletDepositToChangeInto(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "修改失败", "");
        }
        num = insertWalletDepositTurnTo(userWalletCoin, wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "增加失败", "");
        }
        num = insertWalletDepositToChangeInfo(userWalletCoin, wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "增加失败", "");
        }
        deductionPrice = wallet.getAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        num = insertWalletDepositToChangeInfoOrTurnTo(userWalletCoin, wallet, deductionPrice);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用记录失败", "");
        }
        wallet.setId(userWalletCoin.getId());
        wallet.setAmount(deductionPrice);
        num = walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "转入成果", num);
    }

    @Override
    public ApiResponseResult modifyWalletDepositTurnOut(Wallet wallet) throws Exception {

        //walletMapper.lockWalletTable();

        Wallet userWalletCoin = new Wallet();
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到用户钱包下的币种", "");
        }
        List<DictionaryVo> voList = dictionaryMapper.selectDictionaryListById((2), "DEDUCT_FORMALITIES");
        if (null == voList) {
            return ApiResponseResult.build(2010, "error", "未查询到数据字典内容信息", "");
        }
        DictionaryVo dictionaryVo = (DictionaryVo)voList.get(0);
        BigDecimal deductionPrice = userWalletCoin.getAvailableAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        userWalletCoin.setAvailableAmount(userWalletCoin.getAvailableAmount().subtract(deductionPrice));

        int compare = userWalletCoin.getFreeAmount().compareTo(wallet.getAmount());
        if ((compare == 0) || (compare == -1)) {
            return ApiResponseResult.build(2011, "error", "币种数量不足", "");
        }
        Integer num = walletMapper.modifyWalletDepositTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "修改失败", "");
        }
        num = insertWalletDepositTurnTo(userWalletCoin, wallet);
        if (num == 0) {
            return ApiResponseResult.build(2013, "error", "增加失败", "");
        }
        num = insertWalletDepositToChangeInfo(userWalletCoin, wallet);
        if (num == 0) {
            return ApiResponseResult.build(2014, "error", "增加失败", "");
        }
        deductionPrice = wallet.getAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        num = insertWalletDepositToChangeInfoOrTurnTo(userWalletCoin, wallet, deductionPrice);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用记录失败", "");
        }
        wallet.setId(userWalletCoin.getId());
        wallet.setAmount(deductionPrice);
        num = walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "转出成果", num);
    }

    /*public Integer insertUserWalletInfo(Integer userId)
            throws Exception
    {
        List<CoinVo> voList = coinMapper.selectCoinList();
        if (null == voList) {
            return (0);
        }
        Wallet wallet = null;
        Investment investment = null;

        List<Wallet> walletList = new ArrayList();
        List<Investment> investmentList = new ArrayList();
        for (Iterator localIterator = voList.iterator(); localIterator.hasNext();)
        {
            coinVo = (CoinVo)localIterator.next();

            wallet = new Wallet();

            wallet.setCoinName(coinVo.getCoinName());
            wallet.setAddress(ObjectUtils.getUUID());
            wallet.setPrivateKey(ObjectUtils.getUUID());
            wallet.setPasswd(ObjectUtils.getUUID());
            wallet.setKeystore(ObjectUtils.getUUID());
            wallet.setUserId(userId);

            walletList.add(wallet);
        }
        CoinVo coinVo;
        Integer num = walletMapper.insertUserWalletInfo(walletList);
        for (Wallet wallet1 : walletList)
        {
            investment = new Investment();

            investment.setWalletId(wallet1.getId());

            investmentList.add(investment);
        }
        num = investmentMapper.insertUserInvestmentInfo(investmentList);

        return num;
    }*/

    @Transactional
    @Override
    public ApiResponseResult modifyChargeMoneyInfo(Wallet wallet) throws Exception {

        BigDecimal recommendLockRation = RewardConfigureUtils.getInstance().getRecommendLockRation();
        Integer recommendNumber = (RewardConfigureUtils.getInstance().getRecommendNumber());

        //walletMapper.lockWalletTable();

        Wallet userWalletCoin = new Wallet();
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到该用户钱包信息", "");
        }
        List<TranscationVo> transcationVoList = transcationMapper.selectUserCoinTransactionList((1), (1), userWalletCoin
                .getUserId(), wallet.getCoinName());
        if (null == transcationVoList || transcationVoList.size() == 0) {

            String str = "";
        }

        Object voList = dictionaryMapper.selectDictionaryListById((4), "DEDUCT_FORMALITIES");
        if (null == voList) {
            return ApiResponseResult.build(2012, "error", "未查询到数据字典内容信息", "");
        }
        DictionaryVo dictionaryVo = (DictionaryVo)((List)voList).get(0);
        BigDecimal deductionPrice = userWalletCoin.getAvailableAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        userWalletCoin.setAvailableAmount(userWalletCoin.getAvailableAmount().subtract(deductionPrice));

        Integer num = walletMapper.modifyWalletToChangeInto(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "充值失败", "");
        }
        num = insertWalletToChangeInto(wallet, userWalletCoin, userWalletCoin);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "新增充值记录失败", "");
        }
        deductionPrice = wallet.getAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        num = insertWalletRechargeTurnTo(wallet, userWalletCoin, deductionPrice);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用记录失败", "");
        }
        wallet.setId(userWalletCoin.getId());
        wallet.setAmount(deductionPrice);
        num = walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "充值成功", num);
    }

    @Override
    public ApiResponseResult createWalletInfo(User user) throws Exception {

        UserVo userInfo = userMapper.findUserExist(user.getPhone());
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
            //return ApiResponseResult.build(2010, "error", "该用户不存在", "");
        }

        String addressExist = walletMapper.findWalletAddressByUserId(userInfo.getId(), "ETH");
        if (addressExist != null && !addressExist.equals("")) {

            throw new WalletException(WalletEnum.WALLET_REPEAT_INFO);
            //return ApiResponseResult.build(2010, "error", "已添加过该币种", "");
        }

        String uri = "";                //第三方API接口路径
        uri = url + "/register";

        Wallet wallet = new Wallet();
        wallet.setPasswd(user.getPasswd());
        wallet.setUserId(userInfo.getId());

        //Map<String, Object> mapOne = new HashMap();
        Map<String, String> map = new HashMap();
        map.put("phone", user.getPhone());
        map.put("txpw", wallet.getPasswd());

        String json = JSONArray.toJSONString(map);

        String str = HttpUtils.sendPost(uri, json);
        if (str == null || str.equals("") || str.equals("null")) {

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            //return ApiResponseResult.build(2010, "error", "系统异常", "");
        }

        String address = ObjectUtils.getAddress(str);

        if(address == null || address.equals("")){

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            //return ApiResponseResult.build(2010, "error", "系统异常", "");
        }

        Coin coin = this.coinMapper.selectCoinByAddress("☺");
        if (null == coin) {

            throw new WalletException(WalletEnum.WALLET_NOT_EXISTENT_ERROR);
            //return ApiResponseResult.build(2013, "error", "该币种不存在", "");
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

            throw new WalletException(WalletEnum.WALLET_INSERT_FALL);
            //return ApiResponseResult.build(2010, "error", "开通钱包失败", "");
        }
        return ApiResponseResult.build(200, "success", "开通钱包成功", str);
    }

    @Override
    public ApiResponseResult findUserWalletList(Integer currentPage,Integer currentSize,String phone,Integer id,String coinName) throws Exception {

        UserVo userInfo = userMapper.findUserExist(phone);
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
            //return ApiResponseResult.build(2010, "error", "该用户不存在", "");
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
            //return ApiResponseResult.build(2011, "error", "未查询到用户钱包币种信息", "");
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
                //return ApiResponseResult.build(2011, "error", "系统异常", "");
            }

            price = ObjectUtils.getPrice(str);

            //是否能拿到币种金额
            Integer compareZero = price.compareTo(BigDecimal.ZERO);
            if(compareZero == -1){

                throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
                //return ApiResponseResult.build(2011, "error", "系统异常", "");
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
    public ApiResponseResult modifyWithdrawMoney(WalletUtilsVo wallet) throws Exception {

        //查看当前用户是否存在
        UserVo userInfo = userMapper.findUserExist(wallet.getPhone());
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
            //return ApiResponseResult.build(2010, "error", "该用户不存在", "");
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
            //return ApiResponseResult.build(2011, "error", "用户未拥有该币种", "");
        }

        //当前用户转账锁钱包表
        walletMapper.lockWalletTable(userWalletCoin.getId());

        int trun = new BigDecimal(wallet.getValue()).compareTo(BigDecimal.ZERO);
        if (trun == 0 || trun == -1) {

            throw new WalletException(WalletEnum.WALLET_NOT_GT_ZERO);
            //return ApiResponseResult.build(2010, "error", "请输入大于 0 的正数", "");
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
            //return ApiResponseResult.build(2010, "error", "余额不足", "");
        }

        price = ObjectUtils.getPrice(str);
        //看是否出异常

        Integer compareZero = price.compareTo(BigDecimal.ZERO);
        if(compareZero == -1){

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            //return ApiResponseResult.build(2011, "error", "系统异常", "");
        }

        //拿出币种数量和转账数量比较
        int compare = price.compareTo(new BigDecimal(wallet.getValue()));
        if (compare == 0 || compare == -1) {

            throw new WalletException(WalletEnum.WALLET_AMOUNT_INSUFFICIENT);
            //return ApiResponseResult.build(2011, "error", "币种数量不足", "");
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
            //return ApiResponseResult.build(2011, "error", "提币失败", "");
        }

        String hash = ObjectUtils.getHash(str);         //拿出成功的hash

        if(hash != null && hash.equals("-1")){

            throw new WalletException(WalletEnum.WALLET_ABSENTEEISM_REPEAT);
            //return ApiResponseResult.build(2011, "error", "旷工费不足", "");
        }
        if(hash == null || hash.equals("")){

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            //return ApiResponseResult.build(2011, "error", "系统异常", "");
        }

        wallet.setHash(hash);                           //转账成功的hash值

        //新增转账记录
        Integer num = insertWalletTurnTo(wallet, userWalletCoin);
        if (num == 0) {

            throw new WalletException(WalletEnum.WALLET_INSERT_COINNAME);
            //return ApiResponseResult.build(2011, "error", "添加失败", "");
        }
        return ApiResponseResult.build(200, "success", "提币成功", num);
    }

    @Override
    public ApiResponseResult queryContractAddr(String phone, String contractAddr) throws Exception {

        UserVo userInfo = userMapper.findUserExist(phone);
        if (null == userInfo) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
            //return ApiResponseResult.build(2013, "error", "该用户不存在", "");
        }

        ContractRelation relation = contractRelationMapper.findWalletByUserIdAndAddress(userInfo.getId(), contractAddr);
        if (relation != null) {

            throw new WalletException(WalletEnum.WALLET_REPEAT_INFO);
            //return ApiResponseResult.build(2013, "error", "您已添加过该币种", "");
        }

        List<Wallet> walletList = walletMapper.findUserWalletInfo(userInfo.getId());
        if (null == walletList && walletList.size() == 0) {

            throw new WalletException(WalletEnum.WALLET_NOT_LIST_INFO);
            //return ApiResponseResult.build(2013, "error", "该用户还未添加过钱包", "");
        }

        Wallet walletVo = walletList.get(0);

        String uri = "";                //第三方API接口路径

        uri = url + "/token";

        Map<String, Object> mapOne = new HashMap();
        Map<String, Object> mapTwo = new HashMap();

        Map<String, String> map = new HashMap();
        map.put("contractAddr", contractAddr);

        String str = HttpUtils.sendGet(uri, map, 2);
        if (str == null || str.equals("") || str.equals("null")) {

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            //return ApiResponseResult.build(2011, "error", "系统异常", "");
        }

        String coinName = ObjectUtils.getNum(str);

        if (coinName.equals("-1")) {

            throw new WalletException(WalletEnum.WALLET_NOT_EXISTENT_ERROR);
            //return ApiResponseResult.build(Integer.valueOf(2012), "error", "币种不存在", "");
        }
        if (coinName.equals("-2")) {

            throw new WalletException(WalletEnum.WALLET_SYSTEM_ERR);
            //return ApiResponseResult.build(Integer.valueOf(2012), "error", "系统异常", "");
        }

        ContractRelation contractRelation = new ContractRelation();

        Coin coin = coinMapper.selectCoinByAddress(contractAddr);
        if (null == coin) {

            throw new WalletException(WalletEnum.WALLET_NOT_EXISTENT_ERROR);
            //return ApiResponseResult.build(Integer.valueOf(2013), "error", "该币种不存在", "");
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
            //return ApiResponseResult.build(2015, "error", "添加合约币信息失败", "");
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
        if (str == null || str.equals("")) {

            throw new WalletException(WalletEnum.WALLET_NOT_BLOCK_INFO);
        }
        return ApiResponseResult.build(200, "success", "查询阻塞数信息", JSONArray.parseObject(str));

    }

    @Override
    public ApiResponseResult queryUserWalletInfo(String phone, String earnerPhone) throws Exception {

        UserVo user = userMapper.findUserExist(phone);
        if (null == user) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
            //return ApiResponseResult.build(2013, "error", "当前用户不存在", "");
        }
        UserVo earnerUser = userMapper.findUserExist(earnerPhone);
        if (null == earnerUser) {

            throw new WalletException(WalletEnum.WALLET_NOT_BEI_USER_INFO);
            //return ApiResponseResult.build(2013, "error", "被转账用户不存在", "");
        }
        List<Wallet> walletList = walletMapper.findUserWalletInfo(user.getId());
        if (walletList == null || walletList.size() == 0) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_REPEAT);
            //return ApiResponseResult.build(2013, "error", "该用户没有钱包信息", "");
        }
        List<Wallet> earnerWalletList = walletMapper.findUserWalletInfo(earnerUser.getId());
        if (earnerWalletList == null || earnerWalletList.size() == 0) {

            throw new WalletException(WalletEnum.WALLET_NOT_BER_USER_REPEAT);
            //return ApiResponseResult.build(2013, "error", "被转账用户没有钱包信息", "");
        }

        List<WalletStatusVo> voList = new ArrayList();
        WalletStatusVo walletStatusVo = null;

        for (Wallet wallet : walletList) {
            walletStatusVo = new WalletStatusVo();
            for (Wallet wallet1 : earnerWalletList) {

                walletStatusVo.setCoinName(wallet.getCoinName());
                //walletStatusVo.setStatus(false);

                //if (wallet.getCoinName().equals(wallet1.getCoinName())) {

                //walletStatusVo.setStatus(true);
                // break;
                // }
            }
            voList.add(walletStatusVo);
        }

        return ApiResponseResult.build(200, "success", "当前用户与转账用户的币种比较", voList);
    }

    @Override
    public ApiResponseResult findWalletListInfo(String phone) throws Exception {

        UserVo user = userMapper.findUserExist(phone);
        if (null == user) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
            //return ApiResponseResult.build(2013, "error", "当前用户不存在", "");
        }

        List<WalletStatusVo> voList = walletMapper.findWalletListInfo(user.getId());
        if (null == voList || voList.size() == 0) {

            throw new WalletException(WalletEnum.WALLET_NOT_USER_REPEAT);
            //return ApiResponseResult.build(2013, "error", "该用户未拥有币种信息", "");
        }

        return ApiResponseResult.build(200, "success", "用户币种列表", voList);
    }

    @Override
    public ApiResponseResult findWalletAddressByUserId(String phone) throws Exception {

        //查询当前用户是否存在
        UserVo user = userMapper.findUserExist(phone);

        if(null == user){

            throw new WalletException(WalletEnum.WALLET_NOT_USER_INFO);
            //return ApiResponseResult.build(2011,"error","该用户不存在","");
        }

        String address = walletMapper.findWalletAddressByUserId(user.getId(),"ETH");
        if(address == null || address.equals("")){

            throw new WalletException(WalletEnum.WALLET_NOT_INSERT_ETH_INFO);
            //return ApiResponseResult.build(2011,"error","用户未拥有ETH钱包","");
        }

        return ApiResponseResult.build(200,"success","用户ETH钱包地址",address);
    }


    public Integer insertWalletTurnTo(WalletUtilsVo wallet, Wallet userWalletCoin) throws Exception {

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

    public Integer insertWalletRechargeTurnTo(Wallet wallet, Wallet userWalletCoin, BigDecimal deductionPrice) throws Exception {

        Transcation transcation = new Transcation();
        transcation.setUserId(userWalletCoin.getUserId());
        transcation.setCoinName(wallet.getCoinName());
        transcation.setAmount(deductionPrice);
        transcation.setTxType(1);
        transcation.setFrom(userWalletCoin.getAddress());
        transcation.setTo(wallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(1);
        transcation.setRemark(ObjectUtils.getWalletRemark(wallet.getRemark(), transcation.getTxType()));

        Integer num = transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletDeductionTurnTo(Wallet wallet, Wallet userWalletCoin, BigDecimal deductionPrice) throws Exception {

        Transcation transcation = new Transcation();
        transcation.setUserId(userWalletCoin.getUserId());
        transcation.setCoinName(wallet.getCoinName());
        transcation.setAmount(deductionPrice);
        transcation.setTxType(2);
        transcation.setFrom(userWalletCoin.getAddress());
        transcation.setTo(wallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(1);
        transcation.setRemark(ObjectUtils.getWalletRemark(wallet.getRemark(), transcation.getTxType()));

        Integer num = transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletToChangeInto(Wallet wallet, Wallet walletCoin, Wallet userWalletCoin) throws Exception {

        Transcation transcation = new Transcation();
        transcation.setUserId(walletCoin.getUserId());
        transcation.setCoinName(wallet.getCoinName());
        transcation.setAmount(wallet.getAmount());
        transcation.setTxType(7);
        transcation.setFrom(userWalletCoin.getAddress());
        transcation.setTo(wallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(1);
        transcation.setRemark(ObjectUtils.getWalletRemark(wallet.getRemark(), transcation.getTxType()));

        Integer num = transcationMapper.insertWalletToChangeInto(transcation);

        return num;
    }

    public Integer insertWalletDepositTurnTo(Wallet wallet, Wallet walletBean) throws Exception{

        Transcation transcation = new Transcation();
        transcation.setUserId(wallet.getUserId());
        transcation.setCoinName(walletBean.getCoinName());
        transcation.setAmount(walletBean.getAmount());
        transcation.setTxType(2);
        transcation.setFrom(wallet.getAddress());
        transcation.setTo(wallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(1);
        transcation.setRemark(ObjectUtils.getRemark(walletBean.getRemark(), transcation.getTxType()));

        Integer num = transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletDepositToChangeInfo(Wallet wallet, Wallet walletBean) throws Exception {

        Transcation transcation = new Transcation();
        transcation.setUserId(wallet.getUserId());
        transcation.setCoinName(walletBean.getCoinName());
        transcation.setAmount(walletBean.getAmount());
        transcation.setTxType(1);
        transcation.setFrom(wallet.getAddress());
        transcation.setTo(wallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(1);
        transcation.setRemark(ObjectUtils.getRemark(walletBean.getRemark(), transcation.getTxType()));

        Integer num = transcationMapper.insertWalletToChangeInto(transcation);

        return num;
    }

    public Integer insertWalletDepositToChangeInfoOrTurnTo(Wallet wallet, Wallet walletBean, BigDecimal deductionPrice) throws Exception {

        Transcation transcation = new Transcation();
        transcation.setUserId(wallet.getUserId());
        transcation.setCoinName(walletBean.getCoinName());
        transcation.setAmount(deductionPrice);
        transcation.setTxType(6);
        transcation.setFrom(wallet.getAddress());
        transcation.setTo(wallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(1);
        transcation.setRemark(ObjectUtils.getRemark(walletBean.getRemark(), transcation.getTxType()));

        Integer num = transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public static void main(String[] args) throws Exception {

        BigDecimal bigDecimal = new BigDecimal("-1");

        BigDecimal price = new BigDecimal("-1");

        if(bigDecimal.toString().equals("-1")){

            System.out.println("1234");
        }

    }


}

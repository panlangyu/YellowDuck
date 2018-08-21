package com.duck.yellowduck.domain.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.duck.yellowduck.domain.dao.*;
import com.duck.yellowduck.domain.model.model.Investment;
import com.duck.yellowduck.domain.model.model.Transcation;
import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.model.Wallet;
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


    public ApiResponseResult selectUserWalletTotal(Integer userId) throws Exception {

        UserVo user = this.userMapper.findUserById(userId);
        if (null == user) {
            return ApiResponseResult.build(2015, "error", "该用户不存在", "");
        }

        BigDecimal total = this.walletMapper.selectUserWalletTotal(userId);
        if (null == total) {
            total = new BigDecimal(0);
        }

        return ApiResponseResult.build(200, "success", "查询用户钱包币种总额", total);
    }

    public ApiResponseResult selectUserWalletCoinList(Integer currentPage, Integer currentSize,
                                                      Integer userId, String coinName) throws Exception {

        PageHelper.startPage(currentPage, currentSize);

        List<WalletVo> voList = this.walletMapper.selectUserWalletCoinList(currentPage, currentSize, userId, coinName);
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

    public ApiResponseResult modifyWalletTurnOut(Wallet wallet) throws Exception {

        this.walletMapper.lockWalletTable();

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
        List<DictionaryVo> voList = this.dictionaryMapper.selectDictionaryListById(5, "DEDUCT_FORMALITIES");
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

        Integer num = this.walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "转出失败", "");
        }

        wallet.setId(walletCoin.getId());
        num = this.walletMapper.modifyWalletToChangeInto(wallet);
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
        num = this.walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "提币聪明", num);
    }

    public ApiResponseResult selectUserWalletCoinStraightOrInterest(Integer currentPage, Integer currentSize, Integer userId) throws Exception {

        PageHelper.startPage(currentPage, currentSize);

        Map<String, Object> map = new HashMap();

        List<WalletVo> voList = this.walletMapper.selectUserWalletCoinList(currentPage, currentSize, userId, null);
        if (null == voList) {
            return ApiResponseResult.build(2013, "error", "未查询到管理币种列表信息直推和利息", "");
        }
        PageInfo<WalletVo> pageInfo = new PageInfo(voList);

        List<WalletCoinVo> walletCoinVoList = new ArrayList();

        WalletCoinVo walletCoinVo = null;
        for (WalletVo walletVo : voList)
        {
            walletCoinVo = new WalletCoinVo();

            map = this.transcationMapper.selectUserWalletCoinStraightOrInterest(userId, walletVo.getCoinName());

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

    public ApiResponseResult selectYesterdayProfit(Integer userId, Integer coinId) throws Exception {

        Map<String, Object> map = new HashMap();

        Wallet wallet = this.walletMapper.selectUserWalletByCoinId(userId, "");
        if (null == wallet) {
            return ApiResponseResult.build(2013, "error", "未查询到有该币种信息", "");
        }
        map = this.walletMapper.selectYesterdayProfit(userId, coinId);

        List<Map<String, Object>> list = new ArrayList();
        list.add(map);

        return ApiResponseResult.build(200, "success", "管理钱包用户币种昨日收益", list);
    }

    public ApiResponseResult modifyWalletDepositToChangeInto(Wallet wallet)
            throws Exception
    {
        this.walletMapper.lockWalletTable();

        Wallet userWalletCoin = new Wallet();
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到用户钱包下的币种", "");
        }
        List<DictionaryVo> voList = this.dictionaryMapper.selectDictionaryListById(2, "DEDUCT_FORMALITIES");
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
        Integer num = this.walletMapper.modifyWalletDepositToChangeInto(wallet);
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
        num = this.walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "转入成果", num);
    }

    public ApiResponseResult modifyWalletDepositTurnOut(Wallet wallet) throws Exception {

        this.walletMapper.lockWalletTable();

        Wallet userWalletCoin = new Wallet();
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到用户钱包下的币种", "");
        }
        List<DictionaryVo> voList = this.dictionaryMapper.selectDictionaryListById((2), "DEDUCT_FORMALITIES");
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
        Integer num = this.walletMapper.modifyWalletDepositTurnOut(wallet);
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
        num = this.walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "转出成果", num);
    }

    /*public Integer insertUserWalletInfo(Integer userId)
            throws Exception
    {
        List<CoinVo> voList = this.coinMapper.selectCoinList();
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
        Integer num = this.walletMapper.insertUserWalletInfo(walletList);
        for (Wallet wallet1 : walletList)
        {
            investment = new Investment();

            investment.setWalletId(wallet1.getId());

            investmentList.add(investment);
        }
        num = this.investmentMapper.insertUserInvestmentInfo(investmentList);

        return num;
    }*/

    @Transactional
    @Override
    public ApiResponseResult modifyChargeMoneyInfo(Wallet wallet) throws Exception
    {
        BigDecimal recommendLockRation = RewardConfigureUtils.getInstance().getRecommendLockRation();
        Integer recommendNumber = (RewardConfigureUtils.getInstance().getRecommendNumber());

        this.walletMapper.lockWalletTable();

        Wallet userWalletCoin = new Wallet();
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2010, "error", "未查询到该用户钱包信息", "");
        }
        List<TranscationVo> transcationVoList = this.transcationMapper.selectUserCoinTransactionList((1), (1), userWalletCoin
                .getUserId(), wallet.getCoinName());
        if ((null == transcationVoList) || (transcationVoList.size() == 0)) {
            String str = "";
        }
        Object voList = this.dictionaryMapper.selectDictionaryListById((4), "DEDUCT_FORMALITIES");
        if (null == voList) {
            return ApiResponseResult.build(2012, "error", "未查询到数据字典内容信息", "");
        }
        DictionaryVo dictionaryVo = (DictionaryVo)((List)voList).get(0);
        BigDecimal deductionPrice = userWalletCoin.getAvailableAmount().multiply(new BigDecimal(dictionaryVo.getValue()));

        userWalletCoin.setAvailableAmount(userWalletCoin.getAvailableAmount().subtract(deductionPrice));

        Integer num = this.walletMapper.modifyWalletToChangeInto(wallet);
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
        num = this.walletMapper.modifyWalletTurnOut(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2012, "error", "扣除费用失败", "");
        }
        return ApiResponseResult.build(200, "success", "充值成功", num);
    }

    public ApiResponseResult createWalletInfo(User user) throws Exception {
        
        UserVo userInfo = this.userMapper.findUserExist(user.getPhone());
        if (null == userInfo) {
            return ApiResponseResult.build(2010, "error", "该用户不存在", "");
        }
        
        String address = this.walletMapper.findWalletAddressByUserId(userInfo.getId(), "ETH");
        if ((address != null) && (!address.equals(""))) {
            return ApiResponseResult.build(2013, "error", "您已添加过该币种", "");
        }
        String url = "http://39.105.26.249:9090/register";

        Wallet wallet = new Wallet();
        wallet.setPasswd(user.getPasswd());
        wallet.setUserId(userInfo.getId());

        Map<String, Object> mapOne = new HashMap();
        Map<String, String> map = new HashMap();
        map.put("phone", user.getPhone());
        map.put("txpw", wallet.getPasswd());

        String json = JSONArray.toJSONString(map);

        String str = HttpUtils.sendPost(url, json);
        if ((str == null) || (str.equals(""))) {
            return ApiResponseResult.build(2010, "error", "开通钱包失败", "");
        }
        mapOne = (Map)JSONArray.parse(str);

        Map<String, Object> mapTwo = new HashMap();
        for (String string : mapOne.keySet()) {
            if (string.equals("body"))
            {
                mapTwo = (Map)mapOne.get("body");

                wallet.setAddress(mapTwo.get("address").toString());
            }
        }
        wallet.setCoinName("ETH");
        wallet.setPrivateKey(ObjectUtils.getUUID());
        wallet.setKeystore(ObjectUtils.getUUID());
        wallet.setCoinImg("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1701349413,5323685&fm=173&app=25&f=JPEG?w=400&h=240&s=219B1DDD6A2255074C38E0700300D07A");

        Integer num = this.walletMapper.insertWalletInfo(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2010, "error", "开通钱包失败", "");
        }
        return ApiResponseResult.build(200, "success", "开通钱包成功", str);
    }

    public ApiResponseResult findUserWalletList(Integer currentPage, Integer currentSize,
                                                String phone, String coinName) throws Exception {

        UserVo userInfo = this.userMapper.findUserExist(phone);
        if (null == userInfo) {
            return ApiResponseResult.build(2010, "error", "该用户不存在", "");
        }
        PageHelper.startPage(currentPage, currentSize);

        List<WalletListInfo> walletVoList = new ArrayList();

        List<WalletVo> voList = this.walletMapper.selectUserWalletCoinList(currentPage, currentSize, userInfo.getId(), coinName);
        if (null == voList) {
            return ApiResponseResult.build(2011, "error", "未查询到用户钱包必中信息", "");
        }
        Map<String, String> map = new HashMap();

        Map<String, Object> mapPut = new HashMap();
        Map<String, Object> mapThree = new HashMap();
        Map<String, Object> mapFour = new HashMap();

        WalletListInfo walletVo = null;

        String url = "http://39.105.26.249:9090/address";

        String str = "";

        String address = "";
        for (WalletVo vo : voList)
        {
            map = new HashMap();

            walletVo = new WalletListInfo();
            if (!vo.getCoinName().equals("ETH"))
            {
                address = this.walletMapper.findWalletAddressByUserId(vo.getUserId(), "ETH");

                map.put("contractAddr", vo.getContractAddr());
                map.put("from", address);

                url = "http://39.105.26.249:9090/token/balance";

                walletVo.setWalletTotal(new BigDecimal("1"));
            }
            else
            {
                map.put("address", vo.getAddress());

                address = vo.getAddress();

                walletVo.setWalletTotal(new BigDecimal("3000"));
            }
            BigDecimal price = new BigDecimal("0");

            str = HttpUtils.sendGet(url, map, (2));
            if ((str != null) && (!str.equals("")))
            {
                System.out.println(str);

                mapThree = (Map)JSONArray.parse(str);
                for (String string : mapThree.keySet()) {
                    if (string.equals("body"))
                    {
                        mapFour = (Map)mapThree.get("body");

                        price = new BigDecimal(mapFour.get("balance").toString());
                    }
                }
            }
            else
            {
                price = new BigDecimal("0");
            }
            walletVo.setId(vo.getId());
            walletVo.setAddress(address);
            walletVo.setCoinName(vo.getCoinName());

            walletVo.setAmount(price);
            walletVo.setCoinImg(vo.getCoinImg());

            walletVoList.add(walletVo);
        }
        Object pageInfo = new PageInfo(walletVoList);
        PageBean<WalletListInfo> pageBean = new PageBean();
        pageBean.setCurrentPage(currentPage);
        pageBean.setCurrentSize(currentSize);
        pageBean.setTotalNum(((PageInfo)pageInfo).getTotal());
        pageBean.setItems(walletVoList);

        return ApiResponseResult.build(200, "success", "查询用户钱包币种列表数据", pageBean);
    }

    @Transactional
    public ApiResponseResult modifyWithdrawMoney(WalletUtilsVo wallet)
            throws Exception
    {
        UserVo userInfo = this.userMapper.findUserExist(wallet.getPhone());
        if (null == userInfo) {
            return ApiResponseResult.build(2010, "error", "该用户不存在", "");
        }
        this.walletMapper.lockWalletTable();

        int trun = new BigDecimal(wallet.getValue()).compareTo(BigDecimal.ZERO);
        if ((trun == 0) || (trun == -1)) {
            return ApiResponseResult.build(2010, "error", "请输入大于 0 的正数", "");
        }
        Wallet userWalletCoin = this.walletMapper.selectUserWalletByCoinId(userInfo.getId(), wallet.getCoinName());
        if (userWalletCoin == null) {
            return ApiResponseResult.build(2011, "error", "未查询到用户下的币种信息", "");
        }
        String url = "";

        Map<String, String> amountMap = new HashMap();
        Map<String, String> txMap = new HashMap();
        Map<String, Object> mapOne = new HashMap();
        Map<String, Object> mapTwo = new HashMap();
        Map<String, Object> mapThree = new HashMap();
        Map<String, Object> mapFour = new HashMap();
        if ((userWalletCoin.getContractAddr() != null) && (!userWalletCoin.getContractAddr().equals("")))
        {
            url = "http://39.105.26.249:9090/token/balance";
            amountMap.put("contractAddr", userWalletCoin.getContractAddr());
            amountMap.put("from", userWalletCoin.getAddress());
        }
        else
        {
            url = "http://39.105.26.249:9090/address";
            amountMap.put("address", userWalletCoin.getAddress());
        }
        String str = "";
        BigDecimal price = new BigDecimal("0");

        str = HttpUtils.sendGet(url, amountMap, (2));
        if ((str != null) && (!str.equals("")))
        {
            mapThree = (Map)JSONArray.parse(str);
            for (String string : mapThree.keySet()) {
                if (string.equals("body"))
                {
                    mapFour = (Map)mapThree.get("body");

                    price = new BigDecimal(mapFour.get("balance").toString());
                }
            }
        }
        else
        {
            price = new BigDecimal("0");
        }
        int compare = price.compareTo(new BigDecimal(wallet.getValue()));
        if ((compare == 0) || (compare == -1)) {
            return ApiResponseResult.build(2011, "error", "币种数量不足", "");
        }
        url = "http://39.105.26.249:9090/sendTx";

        txMap.put("sign", userWalletCoin.getPasswd());
        txMap.put("to", wallet.getAddress());
        txMap.put("value", wallet.getValue());
        txMap.put("gas", "6050");
        txMap.put("gasPrice", "7810");
        if ((userWalletCoin.getContractAddr() != null) && (!userWalletCoin.getContractAddr().equals("")))
        {
            url = "http://39.105.26.249:9090/token/sendTx";
            txMap.put("contractAddr", userWalletCoin.getContractAddr());
            txMap.put("from", userWalletCoin.getAddress());
        }
        else
        {
            txMap.put("from", userWalletCoin.getAddress());
        }
        String json = JSONArray.toJSONString(txMap);
        str = HttpUtils.sendPost(url, json);
        if ((str == null) || (str.equals(""))) {
            return ApiResponseResult.build(2011, "error", "提币失败", "");
        }
        mapOne = (Map)JSONArray.parse(str);

       /* if ((mapOne == null) || (mapOne.size() == 0)) {
            return ApiResponseResult.build(2011, "error", "余额不足", "");
        }*/

        System.out.println(mapOne);
        for (String string : mapOne.keySet())
        {
            if (string.equals("tx"))
            {
                mapTwo = (Map)mapOne.get("tx");
                wallet.setHash(mapTwo.get("result").toString());
            }
            if (string.equals("body"))
            {
                mapTwo = (Map)mapOne.get("body");
                wallet.setHash(mapTwo.get("hash").toString());
            }
        }
        Integer num = insertWalletTurnTo(wallet, userWalletCoin);
        if (num == 0) {
            return ApiResponseResult.build(2011, "error", "新增交易记录失败", "");
        }
        return ApiResponseResult.build(200, "success", "提币成功", num);
    }

    public ApiResponseResult queryContractAddr(String phone, String contractAddr)
            throws Exception
    {
        UserVo userInfo = this.userMapper.findUserExist(phone);
        if (null == userInfo) {
            return ApiResponseResult.build(2013, "error", "该用户不存在", "");
        }
        Wallet walletByAddress = this.walletMapper.findWalletByUserIdAndAddress(userInfo.getId(), contractAddr);
        if (walletByAddress != null) {
            return ApiResponseResult.build(2013, "error", "您已添加过该币种", "");
        }
        List<Wallet> walletList = this.walletMapper.findUserWalletInfo(userInfo.getId());
        if ((null == walletList) && (walletList.size() == 0)) {
            return ApiResponseResult.build(2013, "error", "该用户还未添加过钱包", "");
        }
        Wallet walletVo = (Wallet)walletList.get(0);

        String url = "http://39.105.26.249:9090/token";

        Map<String, Object> mapOne = new HashMap();
        Map<String, Object> mapTwo = new HashMap();

        Map<String, String> map = new HashMap();
        map.put("contractAddr", contractAddr);

        String str = HttpUtils.sendGet(url, map, 2);
        if ((str == null) || (str.equals(""))) {
            return ApiResponseResult.build(2011, "error", "出现异常", "");
        }
        mapOne = (Map)JSONArray.parse(str);
        if ((mapOne.get("type").toString().equals("error")) && (mapOne.get("code").toString().equals("1"))) {
            return ApiResponseResult.build(2012, "error", "币种不存在", "");
        }
        if ((mapOne.get("type").toString().equals("error")) && (!mapOne.get("code").toString().equals("1"))) {
            return ApiResponseResult.build(2012, "error", "系统异常", "");
        }
        Wallet wallet = new Wallet();
        if (mapOne.get("type").toString().equals("ok"))
        {
            for (String string : mapOne.keySet()) {
                if (string.equals("body"))
                {
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
        }
        Integer num = this.walletMapper.insertWalletInfo(wallet);
        if (num == 0) {
            return ApiResponseResult.build(2015, "error", "添加合约币信息失败", "");
        }
        return ApiResponseResult.build(200, "success", "添加合约币信息成功", num);
    }

    public ApiResponseResult queryAccountList() throws Exception {

        String url = "http://39.105.26.249:9090/accounts";

        Map<String, String> map = new HashMap();
        String str = HttpUtils.sendGet(url, map, 2);
        if ((str == null) || (str.equals(""))) {
            return ApiResponseResult.build(2011, "error", "未查询到账户数据", "");
        }
        return ApiResponseResult.build(200, "success", "查询所有钱包账户", str);
    }

    public ApiResponseResult blockNumber()
    {
        String url = "http://39.105.26.249:9090/blockNumber";

        Map<String, String> map = new HashMap();

        String str = HttpUtils.sendGet(url, map, (2));
        if ((str == null) || (str.equals(""))) {
            return ApiResponseResult.build(2011, "error", "未查询到阻塞数信息", "");
        }
        return ApiResponseResult.build(200, "success", "查询阻塞数信息", JSONArray.parseObject(str));
    }

    public ApiResponseResult queryUserWalletInfo(String phone, String earnerPhone)
            throws Exception
    {
        UserVo user = this.userMapper.findUserExist(phone);
        if (null == user) {
            return ApiResponseResult.build(2013, "error", "当前用户不存在", "");
        }
        UserVo earnerUser = this.userMapper.findUserExist(earnerPhone);
        if (null == earnerUser) {
            return ApiResponseResult.build(2013, "error", "被转账用户不存在", "");
        }
        List<Wallet> walletList = this.walletMapper.findUserWalletInfo(user.getId());
        if ((walletList == null) || (walletList.size() == 0)) {
            return ApiResponseResult.build(2013, "error", "该用户没有钱包信息", "");
        }
        List<Wallet> earnerWalletList = this.walletMapper.findUserWalletInfo(earnerUser.getId());
        if ((earnerWalletList == null) || (earnerWalletList.size() == 0)) {
            return ApiResponseResult.build(2013, "error", "被转账用户没有钱包信息", "");
        }
        List<WalletStatusVo> voList = new ArrayList();
        WalletStatusVo walletStatusVo = null;
        for (Wallet wallet : walletList)
        {
            walletStatusVo = new WalletStatusVo();
            for (Wallet wallet1 : earnerWalletList)
            {
                walletStatusVo.setCoinName(wallet.getCoinName());
                walletStatusVo.setStatus(false);
                if (wallet.getCoinName().equals(wallet1.getCoinName()))
                {
                    walletStatusVo.setStatus(true);
                    break;
                }
            }
            voList.add(walletStatusVo);
        }
        return ApiResponseResult.build(200, "success", "当前用户与转账用户的币种比较", voList);
    }

    public ApiResponseResult chatAndTransfer(WalletVXUtilsVo wallet)
            throws Exception
    {
        UserVo user = this.userMapper.findUserExist(wallet.getPhone());
        if (null == user) {
            return ApiResponseResult.build(2013, "error", "当前用户不存在", "");
        }
        UserVo earnerUser = this.userMapper.findUserExist(wallet.getEarnerPhone());
        if (null == earnerUser) {
            return ApiResponseResult.build(2013, "error", "被转账用户不存在", "");
        }
        Wallet userWallet = this.walletMapper.selectUserWalletByCoinId(user.getId(), wallet.getCoinName());
        if (null == userWallet) {
            return ApiResponseResult.build(2011, "error", "用户未拥有该币种", "");
        }
        Wallet earnerWallet = this.walletMapper.selectUserWalletByCoinId(earnerUser.getId(), wallet.getCoinName());
        if (null == earnerWallet) {
            return ApiResponseResult.build(2011, "error", "被转账用户未拥有该币种", "");
        }
        this.walletMapper.lockWalletTable();

        int trun = new BigDecimal(wallet.getValue()).compareTo(BigDecimal.ZERO);
        if ((trun == 0) || (trun == -1)) {
            return ApiResponseResult.build(2010, "error", "请输入大于 0 的正数", "");
        }
        String url = "";

        Map<String, String> amountMap = new HashMap();
        Map<String, String> txMap = new HashMap();
        Map<String, Object> mapOne = new HashMap();
        Map<String, Object> mapTwo = new HashMap();
        Map<String, Object> mapThree = new HashMap();
        Map<String, Object> mapFour = new HashMap();
        if ((userWallet.getContractAddr() != null) && (!userWallet.getContractAddr().equals("")))
        {
            url = "http://39.105.26.249:9090/token/balance";

            amountMap.put("from", userWallet.getAddress());
            amountMap.put("contractAddr", userWallet.getContractAddr());
        }
        else
        {
            url = "http://39.105.26.249:9090/address";
            amountMap.put("address", userWallet.getAddress());
        }
        String str = "";
        BigDecimal price = new BigDecimal("0");

        str = HttpUtils.sendGet(url, amountMap, (2));
        if ((str != null) && (!str.equals("")))
        {
            mapOne = (Map)JSONArray.parse(str);
            for (String string : mapOne.keySet()) {
                if (string.equals("body"))
                {
                    mapTwo = (Map)mapOne.get("body");

                    price = new BigDecimal(mapTwo.get("balance").toString());
                }
            }
        }
        else
        {
            price = new BigDecimal("0");
        }
        return null;
    }

    public Integer insertWalletTurnTo(WalletUtilsVo wallet, Wallet userWalletCoin)
            throws Exception
    {
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

        Integer num = this.transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletRechargeTurnTo(Wallet wallet, Wallet userWalletCoin, BigDecimal deductionPrice)
            throws Exception
    {
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

        Integer num = this.transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletDeductionTurnTo(Wallet wallet, Wallet userWalletCoin, BigDecimal deductionPrice)
            throws Exception
    {
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

        Integer num = this.transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletToChangeInto(Wallet wallet, Wallet walletCoin, Wallet userWalletCoin)
            throws Exception
    {
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

        Integer num = this.transcationMapper.insertWalletToChangeInto(transcation);

        return num;
    }

    public Integer insertWalletDepositTurnTo(Wallet wallet, Wallet walletBean)
            throws Exception
    {
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

        Integer num = this.transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletDepositToChangeInfo(Wallet wallet, Wallet walletBean)
            throws Exception
    {
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

        Integer num = this.transcationMapper.insertWalletToChangeInto(transcation);

        return num;
    }

    public Integer insertWalletDepositToChangeInfoOrTurnTo(Wallet wallet, Wallet walletBean, BigDecimal deductionPrice)
            throws Exception
    {
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

        Integer num = this.transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public static void main(String[] args)
            throws Exception
    {
        String str = "0x00000000000000000000000000000000000000000000003635c9adc5dea00000";

        String hex = "0xfff";
        Integer x = (Integer.parseInt(str, 16));
        System.out.println(x);
    }


}

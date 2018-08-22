package com.duck.yellowduck.domain.service.impl;

import com.duck.yellowduck.domain.dao.DictionaryMapper;
import com.duck.yellowduck.domain.dao.DirectPrizeMapper;
import com.duck.yellowduck.domain.dao.InvestmentMapper;
import com.duck.yellowduck.domain.dao.TranscationMapper;
import com.duck.yellowduck.domain.dao.WalletMapper;
import com.duck.yellowduck.domain.model.model.DirectPrize;
import com.duck.yellowduck.domain.model.model.Investment;
import com.duck.yellowduck.domain.model.model.Transcation;
import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.model.Wallet;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.model.vo.DirectPrizeVo;
import com.duck.yellowduck.domain.model.vo.WalletVo;
import com.duck.yellowduck.domain.service.DirectPrizeService;
import com.duck.yellowduck.publics.ObjectUtils;
import com.duck.yellowduck.publics.RewardConfigureUtils;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 投资Service
 */
@Service
public class DirectPrizeServiceImpl implements DirectPrizeService {

    private Logger logger = Logger.getLogger("DirectPrizeServiceImpl");

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");

    private BigDecimal dayLimit = null;

    @Autowired
    private DirectPrizeMapper directPrizeMapper;

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Autowired
    private TranscationMapper transcationMapper;

    @Autowired
    private InvestmentMapper investmentMapper;

    public void timedTask()
            throws Exception
    {
        this.dayLimit = RewardConfigureUtils.getInstance().getDayLimit();

        int compare = this.dayLimit.compareTo(BigDecimal.ZERO);
        if (compare == 1) {
            interestAward();
        }
        if (compare == 1) {
            directPrize();
        }
        if (compare == 1) {
            dynamicAward();
        }
    }

    @Transactional
    public ApiResponseResult interestAward() throws Exception {

        BigDecimal staticGreaterNumber = RewardConfigureUtils.getInstance().getStaticGreaterNumber();
        BigDecimal staticGreaterRation = RewardConfigureUtils.getInstance().getStaticGreaterRation();
        BigDecimal staticLessNumber = RewardConfigureUtils.getInstance().getStaticLessNumber();
        BigDecimal staticLessRation = RewardConfigureUtils.getInstance().getStaticLessRation();

        this.walletMapper.lockWalletTable();

        List<Wallet> walletList = this.walletMapper.selectUserWalletInterest();
        if (null == walletList) {
            return ApiResponseResult.build(Integer.valueOf(2012), "error", "未查询到未超过3天提取利息的钱包信息", "");
        }
        BigDecimal deductionPrice = null;

        Wallet wallet = null;

        Integer num = null;

        BigDecimal sum = null;

        Boolean flag = Boolean.valueOf(false);
        for (Wallet walletInfo : walletList)
        {
            int greaterThan = walletInfo.getAmount().compareTo(staticGreaterNumber);
            if (greaterThan == -1) {
                deductionPrice = staticLessRation.divide(new BigDecimal("100"));
            }
            if ((greaterThan == 1) || (greaterThan == 0)) {
                deductionPrice = staticGreaterRation.divide(new BigDecimal("100"));
            }
            try
            {
                sum = walletInfo.getAmount().multiply(deductionPrice);
                int compare = sum.compareTo(this.dayLimit);

                wallet = new Wallet();
                wallet.setUserId(walletInfo.getUserId());
                if ((compare == 1) || (compare == 0))
                {
                    wallet.setAmount(this.dayLimit);
                    flag = Boolean.valueOf(true);
                    this.dayLimit = this.dayLimit.subtract(wallet.getAmount());
                }
                else
                {
                    wallet.setAmount(sum);
                    this.dayLimit = this.dayLimit.subtract(sum);
                }
                Wallet userWallet = new Wallet();
                if (null != userWallet)
                {
                    Investment investment = this.investmentMapper.selectInvestmentByWalletId(userWallet.getId());
                    if (null != investment)
                    {
                        wallet.setId(userWallet.getId());
                        num = this.walletMapper.modifyUserWalletInterest(wallet);

                        investment.setInterests(wallet.getAmount());
                        investment.setRecommend(null);
                        investment.setDynamicAward(null);
                        num = this.investmentMapper.modifyInvestmentInfo(investment);

                        num = insertWalletInterestTurnTo(userWallet, wallet.getAmount());

                        num = insertWalletInterestToChargeTo(userWallet, wallet.getAmount());
                        if (flag.booleanValue()) {
                            break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.logger.warning("用户编号" + wallet.getUserId() + "币种编号" + wallet.getCoinName() + ",利息声息多少" + wallet
                        .getAmount() + "当前时间" + this.sdf.format(new Date()));
            }
        }
        return ApiResponseResult.build(Integer.valueOf(200), "success", "今天利息生息已完成", num);
    }

    @Transactional
    public ApiResponseResult directPrize()
            throws Exception
    {
        BigDecimal recommendUnLockRation = RewardConfigureUtils.getInstance().getRecommendUnLockRation();

        this.walletMapper.lockWalletTable();

        List<DirectPrizeVo> voList = this.directPrizeMapper.selectDirectPrizeList();
        if (null == voList) {
            return ApiResponseResult.build(Integer.valueOf(2012), "error", "未查询到直推奖返还", "");
        }
        BigDecimal deductionPrice = recommendUnLockRation.divide(new BigDecimal("100"));

        Wallet wallet = null;

        DirectPrize directPrize = null;

        Integer num = null;

        BigDecimal sum = null;

        Boolean flag = Boolean.valueOf(false);
        for (DirectPrizeVo directPrizeVo : voList)
        {
            wallet = new Wallet();
            wallet.setUserId(directPrizeVo.getRefereeId());

            directPrize = new DirectPrize();
            directPrize.setId(directPrizeVo.getId());

            sum = directPrizeVo.getAmount().multiply(deductionPrice);
            int compare = sum.compareTo(this.dayLimit);
            if ((compare == 1) || (compare == 0)) {
                wallet.setAmount(this.dayLimit);
                directPrize.setAmount(this.dayLimit);
                this.dayLimit = this.dayLimit.subtract(wallet.getAmount());
                flag = Boolean.valueOf(true);
            }
            else
            {
                wallet.setAmount(sum);
                directPrize.setAmount(sum);
                this.dayLimit = this.dayLimit.subtract(sum);
            }
            try {
                Wallet userWallet = new Wallet();
                if (null == userWallet) {
                    continue;
                }
                Wallet directWallet = this.walletMapper.selectUserWalletByCoinId(directPrizeVo.getCoverRefereeId(), "");
                if (null == directWallet) {
                    continue;
                }
                Investment investment = this.investmentMapper.selectInvestmentByWalletId(userWallet.getId());
                if (null == investment) {
                    continue;
                }
                num = this.directPrizeMapper.modifyDirectPrizeInfo(directPrize);

                investment.setInterests(null);
                investment.setRecommend(wallet.getAmount());
                investment.setDynamicAward(null);
                num = this.investmentMapper.modifyInvestmentInfo(investment);

                directWallet.setAmount(directPrizeVo.getAmountAvailable());
                num = insertWalletTurnTo(userWallet, directWallet, wallet.getAmount());

                num = insertWalletToChargeTo(userWallet, directWallet, directPrize.getAmount());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.logger.warning("用户编号" + directPrizeVo.getCoverRefereeId() + ",币种编号" + directPrizeVo.getCoinId() + ",返钱多少" + wallet
                        .getAmount() + ",当前时间" + this.sdf.format(new Date()));
            }
            if (flag.booleanValue()) {
                break;
            }
        }
        return ApiResponseResult.build(Integer.valueOf(200), "success", "今天任务返还完毕", num);
    }

    @Transactional
    public ApiResponseResult dynamicAward()
            throws Exception
    {
        this.walletMapper.lockWalletTable();

        BigDecimal dynamicPrincipal = RewardConfigureUtils.getInstance().getDynamicPrincipal();
        BigDecimal dynamicRation = RewardConfigureUtils.getInstance().getDynamicRation();
        Integer outRecommendNumbers = RewardConfigureUtils.getInstance().getOutRecommendNumbers();
        BigDecimal outPrincipalLimint = RewardConfigureUtils.getInstance().getOutPrincipalLimint();
        BigDecimal outRecommendMultiple = RewardConfigureUtils.getInstance().getOutRecommendMultiple();
        BigDecimal outMultiple = RewardConfigureUtils.getInstance().getOutMultiple();
        Integer factorialNumber = RewardConfigureUtils.getInstance().getFactorialNumber();

        List<User> userListInfo = new ArrayList();

        List<User> relationshipList = new ArrayList();

        List<WalletVo> voList = new ArrayList();

        List<WalletVo> newWalletList = new ArrayList();

        List<WalletVo> walletVoList = new ArrayList();

        Wallet wallet = null;

        BigDecimal deductionPrice = dynamicRation.divide(new BigDecimal("100"));

        Integer num = null;

        BigDecimal sum = null;

        Boolean flag = Boolean.valueOf(false);

        Integer count = Integer.valueOf(0);

        return null;
    }

    public Integer insertWalletTurnTo(Wallet userWallet, Wallet directWallet, BigDecimal deductionPrice)
            throws Exception
    {
        Transcation transcation = new Transcation();
        transcation.setUserId(directWallet.getUserId());

        transcation.setAmount(deductionPrice);
        transcation.setTxType(Integer.valueOf(3));
        transcation.setFrom(directWallet.getAddress());
        transcation.setTo(userWallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(Integer.valueOf(1));

        transcation.setRemark(ObjectUtils.getRemark(directWallet.getRemark(), transcation.getTxType()));

        Integer num = this.transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletToChargeTo(Wallet userWallet, Wallet directWallet, BigDecimal deductionPrice)
            throws Exception
    {
        Transcation transcation = new Transcation();
        transcation.setUserId(userWallet.getUserId());

        transcation.setTxType(Integer.valueOf(3));
        transcation.setFrom(directWallet.getAddress());
        transcation.setTo(userWallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(Integer.valueOf(1));

        transcation.setRemark(ObjectUtils.getRemark(userWallet.getRemark(), transcation.getTxType()));

        Integer num = this.transcationMapper.insertWalletToChangeInto(transcation);

        return num;
    }

    public Integer insertWalletInterestTurnTo(Wallet userWallet, BigDecimal deductionPrice)
            throws Exception
    {
        Transcation transcation = new Transcation();
        transcation.setUserId(userWallet.getUserId());

        transcation.setAmount(deductionPrice);
        transcation.setTxType(Integer.valueOf(4));
        transcation.setFrom(userWallet.getAddress());
        transcation.setTo(userWallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(Integer.valueOf(1));

        transcation.setRemark(ObjectUtils.getRemark(userWallet.getRemark(), transcation.getTxType()));

        Integer num = this.transcationMapper.insertWalletTurnOut(transcation);

        return num;
    }

    public Integer insertWalletInterestToChargeTo(Wallet userWallet, BigDecimal deductionPrice)
            throws Exception
    {
        Transcation transcation = new Transcation();
        transcation.setUserId(userWallet.getUserId());

        transcation.setAmount(deductionPrice);
        transcation.setTxType(Integer.valueOf(4));
        transcation.setFrom(userWallet.getAddress());
        transcation.setTo(userWallet.getAddress());
        transcation.setHash(ObjectUtils.getUUID());
        transcation.setTxStatus(Integer.valueOf(1));

        transcation.setRemark(ObjectUtils.getRemark(userWallet.getRemark(), transcation.getTxType()));

        Integer num = this.transcationMapper.insertWalletToChangeInto(transcation);

        return num;
    }

    public static void main(String[] args)
    {
        for (int i = 1; i <= 100; i++) {
            if (i % 5 == 0) {
                System.out.println(i);
            }
        }
    }
}

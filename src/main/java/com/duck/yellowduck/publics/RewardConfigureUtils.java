package com.duck.yellowduck.publics;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

public class RewardConfigureUtils
{
    static RewardConfigureUtils configureUtils;
    private BigDecimal staticGreaterNumber;
    private BigDecimal staticGreaterRation;
    private BigDecimal staticLessNumber;
    private BigDecimal staticLessRation;
    private BigDecimal recommendLockRation;
    private BigDecimal recommendUnLockRation;
    private int recommendNumber;
    private BigDecimal dynamicPrincipal;
    private BigDecimal dynamicRation;
    private BigDecimal dayLimit;
    private Integer outRecommendNumbers;
    private BigDecimal outPrincipalLimint;
    private BigDecimal outRecommendMultiple;
    private BigDecimal outMultiple;
    private Integer factorialNumber;

    public static RewardConfigureUtils getInstance()
    {
        if (configureUtils == null) {
            try
            {
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("reward.configure");
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                configureUtils = (RewardConfigureUtils)new Gson().fromJson(sb.toString(), RewardConfigureUtils.class);
            }
            catch (Exception localException) {}
        }
        return configureUtils;
    }

    public void update()
    {
        try
        {
            configureUtils = this;
            FileOutputStream fileOutputStream = new FileOutputStream(Thread.currentThread().getContextClassLoader().getResource("reward.configure").getFile());
            fileOutputStream.write(new Gson().toJson(this).getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static RewardConfigureUtils getConfigureUtils()
    {
        return configureUtils;
    }

    public static void setConfigureUtils(RewardConfigureUtils configureUtils)
    {
        configureUtils = configureUtils;
    }

    public Integer getFactorialNumber()
    {
        return this.factorialNumber;
    }

    public void setFactorialNumber(Integer factorialNumber)
    {
        this.factorialNumber = factorialNumber;
    }

    public BigDecimal getStaticGreaterNumber()
    {
        return this.staticGreaterNumber;
    }

    public void setStaticGreaterNumber(BigDecimal staticGreaterNumber)
    {
        this.staticGreaterNumber = staticGreaterNumber;
    }

    public BigDecimal getStaticGreaterRation()
    {
        return this.staticGreaterRation;
    }

    public void setStaticGreaterRation(BigDecimal staticGreaterRation)
    {
        this.staticGreaterRation = staticGreaterRation;
    }

    public BigDecimal getStaticLessNumber()
    {
        return this.staticLessNumber;
    }

    public void setStaticLessNumber(BigDecimal staticLessNumber)
    {
        this.staticLessNumber = staticLessNumber;
    }

    public BigDecimal getStaticLessRation()
    {
        return this.staticLessRation;
    }

    public void setStaticLessRation(BigDecimal staticLessRation)
    {
        this.staticLessRation = staticLessRation;
    }

    public BigDecimal getRecommendLockRation()
    {
        return this.recommendLockRation;
    }

    public void setRecommendLockRation(BigDecimal recommendLockRation)
    {
        this.recommendLockRation = recommendLockRation;
    }

    public BigDecimal getRecommendUnLockRation()
    {
        return this.recommendUnLockRation;
    }

    public void setRecommendUnLockRation(BigDecimal recommendUnLockRation)
    {
        this.recommendUnLockRation = recommendUnLockRation;
    }

    public int getRecommendNumber()
    {
        return this.recommendNumber;
    }

    public void setRecommendNumber(int recommendNumber)
    {
        this.recommendNumber = recommendNumber;
    }

    public BigDecimal getDynamicPrincipal()
    {
        return this.dynamicPrincipal;
    }

    public void setDynamicPrincipal(BigDecimal dynamicPrincipal)
    {
        this.dynamicPrincipal = dynamicPrincipal;
    }

    public BigDecimal getDynamicRation()
    {
        return this.dynamicRation;
    }

    public void setDynamicRation(BigDecimal dynamicRation)
    {
        this.dynamicRation = dynamicRation;
    }

    public BigDecimal getDayLimit()
    {
        return this.dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit)
    {
        this.dayLimit = dayLimit;
    }

    public Integer getOutRecommendNumbers()
    {
        return this.outRecommendNumbers;
    }

    public void setOutRecommendNumbers(Integer outRecommendNumbers)
    {
        this.outRecommendNumbers = outRecommendNumbers;
    }

    public BigDecimal getOutPrincipalLimint()
    {
        return this.outPrincipalLimint;
    }

    public void setOutPrincipalLimint(BigDecimal outPrincipalLimint)
    {
        this.outPrincipalLimint = outPrincipalLimint;
    }

    public BigDecimal getOutRecommendMultiple()
    {
        return this.outRecommendMultiple;
    }

    public void setOutRecommendMultiple(BigDecimal outRecommendMultiple)
    {
        this.outRecommendMultiple = outRecommendMultiple;
    }

    public BigDecimal getOutMultiple()
    {
        return this.outMultiple;
    }

    public void setOutMultiple(BigDecimal outMultiple)
    {
        this.outMultiple = outMultiple;
    }
}

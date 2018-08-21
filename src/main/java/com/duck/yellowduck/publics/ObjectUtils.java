package com.duck.yellowduck.publics;

import java.util.UUID;

public class ObjectUtils
{
    public static String getWalletRemark(String remark, Integer typeCode)
    {
        if ((remark == null) || (remark.equals(""))) {
            switch (typeCode.intValue())
            {
                case 1:
                    remark = "转入";
                    break;
                case 2:
                    remark = "转出";
                    break;
                default:
                    remark = "只支持以上两种类型";
            }
        }
        return remark;
    }

    public static String getRemark(String remark, Integer typeCode)
    {
        if ((remark != null) && (!remark.equals("")))
        {
            if (typeCode.intValue() == 2) {
                remark = "转出";
            }
            return remark;
        }
        if ((remark == null) || (remark.equals(""))) {
            switch (typeCode.intValue())
            {
                case 1:
                    remark = "����,����(freeAmount)����,����(availableAmount)����,��������";
                    break;
                case 2:
                    remark = "����,����(freeAmount)����,����(availableAmount)����,��������";
                    break;
                case 3:
                    remark = "����";
                    break;
                case 4:
                    remark = "����";
                    break;
                case 5:
                    remark = "������";
                    break;
                case 6:
                    remark = "����������";
                    break;
                default:
                    remark = "����������������";
            }
        }
        return remark;
    }

    public static String getUUID()
    {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        return uuid;
    }
}

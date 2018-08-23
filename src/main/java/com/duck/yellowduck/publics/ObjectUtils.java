package com.duck.yellowduck.publics;

import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 辅助工具类
 */
public class ObjectUtils {


    /**
     * 用户钱包总额
     * @param str
     * @return
     */
    public static BigDecimal getPrice(String str){

        BigDecimal price = new BigDecimal("0");

        //JSONObject jsonObject = new JSONObject();

        if(str != null && !str.equals("") && !str.equals("null")){

            JSONObject body = JSONObject.parseObject(str);

            System.out.println(body.get("body"));

            if(body.get("body") != null && !body.get("body").equals("")){

                JSONObject json = (JSONObject)body.get("body");

                if(json.get("error") != null && !json.get("error").equals("")){

                    price = new BigDecimal("-1");
                    return price;
                }

                price = new BigDecimal(json.get("balance").toString());
            }
        }
        return price;
    }

    /**
     * 获取转账成功的hash值
     * @param str
     * @return
     */
    public static String getHash(String str){

        String hash = "";

        if(str != null && !str.equals("") && !str.equals("null")){

            JSONObject body = JSONObject.parseObject(str);

            System.out.println(body.get("body"));

            if(body.get("body") != null && !body.get("body").equals("")){

                JSONObject json = (JSONObject)body.get("body");

                if(json.get("error") != null && !json.get("error").equals("")){

                    JSONObject error = (JSONObject)json.get("error");

                    if(error.get("message").toString().indexOf("price") != -1){

                        hash = "-1";
                    }

                    return hash;
                }

                hash = json.get("hash").toString();

            }

            if(body.get("tx") != null && !body.get("tx").equals("")){

                JSONObject json = (JSONObject)body.get("tx");

                if(json.get("error") != null && !json.get("error").equals("")){

                    JSONObject error = (JSONObject)json.get("error");

                    if(error.get("message").toString().indexOf("price") != -1){

                        hash = "-1";
                        return hash;
                    }

                    return hash;
                }

                hash = json.get("result").toString();
            }

        }
        return hash;
    }



    public static String getWalletRemark(String remark, Integer typeCode){
        if ((remark == null) || (remark.equals(""))) {
            switch (typeCode)
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
            if (typeCode == 2) {
                remark = "转出";
            }
            return remark;
        }
        if ((remark == null) || (remark.equals(""))) {
            switch (typeCode) {
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

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        return uuid;
    }


    public static void main(String[] args) {

        String error = "{\"error\":{\"code\":-32000,\"message\":\"insufficient funds for gas * price + value\"}}";

        JSONObject jsonObject = JSONObject.parseObject(error);
        JSONObject body = (JSONObject)jsonObject.get("error");


        if(body.get("message").toString().indexOf("price")!=-1){

            System.out.println(body.get("message"));
            System.out.println("123");
        }



    }


}

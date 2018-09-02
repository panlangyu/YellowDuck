package com.duck.yellowduck.publics;


import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 处理业务冗余代码
 */
public class ObjectUtils {

    /**
     * 返回錢包的地址
     * @param str
     * @return
     */
    public static String getAddress(String str){

        String hash = "";

        JSONObject json = JSONObject.parseObject(str);

        System.out.println(json);

        if(json.getString("type").equals("error")){

            return hash;
        }

        JSONObject body = (JSONObject)json.get("body");

        if(body.toString() != null && !body.toString().equals("") && !body.toString().equals("null")){

            hash = body.getString("address");
        }

        return hash;
    }


    /**
     * 用户钱包总额
     * @param str
     * @return
     */
    public static BigDecimal getPrice(String str){

        BigDecimal price = new BigDecimal("0");

        // if(str != null && !str.equals("") && !str.equals("null")){

        JSONObject body = JSONObject.parseObject(str);

        System.out.println(body.get("body"));

        if(body.get("body") != null && !body.get("body").equals("") && !body.get("body").toString().equals("{}")){

            JSONObject json = (JSONObject)body.get("body");

            if(json.get("error") != null && !json.get("error").equals("")){

                price = new BigDecimal("-1");
                return price;
            }

            price = new BigDecimal(json.get("balance").toString());
        }
        // }
        return price;
    }

    /**
     * 获取转账成功的hash值
     * @param str
     * @return
     */
    public static String getHash(String str){

        String hash = "";

        //if(str != null && !str.equals("") && !str.equals("null")){

        JSONObject body = JSONObject.parseObject(str);

        System.out.println(body.get("body"));

        if(body.get("body") != null && !body.get("body").equals("") && !body.get("body").toString().equals("{}")){

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

        if(body.get("tx") != null && !body.get("tx").equals("") && !body.get("tx").toString().equals("{}")){

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

        // }
        return hash;
    }

    /**
     *
     * @param str
     * @return
     */
    public static String getNum(String str){

        String coinName = "";

        JSONObject json = JSONObject.parseObject(str);

        System.out.println(json);

        if (json.get("type").toString().equals("ok")) {

            JSONObject body = (JSONObject)json.get("body");

            if (body.toString().equals("body")) {

                coinName = body.get("symbol").toString();
                return coinName;
            }
        }
        if (json.get("type").toString().equals("error") && json.get("code").toString().equals("1")) {

            coinName = "-1";
            return coinName;
        }
        if (json.get("type").toString().equals("error") && !json.get("code").toString().equals("1")) {

            coinName = "-2";
            return coinName;
        }

        return coinName;
    }







    /**
     * 处理转账信息的备注, 如果没填写备注信息的话,分配类型名称
     * @param remark
     * @param typeCode
     * @return
     */
    public static String getWalletRemark(String remark,Integer typeCode){

        if(remark == null || remark.equals("")){
            switch (typeCode){

                case 1:
                    remark = "转入";
                    break;
                case 2:
                    remark = "转出";
                    break;
                case 6:
                    remark = "扣取手续费";
                    break;
                default:
                    remark = "只有以上三种类型";
                    break;
            }
        }

        return remark;
    }


    /**
     * 处理转账信息的备注, 如果没填写备注信息的话,分配类型名称
     * @param remark
     * @param typeCode
     * @return
     */
    public static String getRemark(String remark,Integer typeCode){

        if(remark != null && !remark.equals("")){

            if(typeCode == 2){
                remark = "转出";
            }
            return remark;
        }


        if(remark == null || remark.equals("")){
            switch (typeCode){

                case 1:
                    remark = "转入,冻结(freeAmount)增加,可用(availableAmount)减少,本金不变";
                    break;
                case 2:
                    remark = "转出,冻结(freeAmount)减少,可用(availableAmount)增加,本金不变";
                    break;
                case 3:
                    remark = "直推";
                    break;
                case 4:
                    remark = "利息";
                    break;
                case 5:
                    remark = "团队奖";
                    break;
                case 6:
                    remark = "扣取手续费";
                    break;
                default:
                    remark = "只有以上六种类型";
                    break;
            }

        }

        return remark;
    }


    /**
     * 生成UUID
     * @return
     */
    public static String getUUID(){

        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        return uuid;
    }

    public static void main(String[] args) {


        String message = "{\"body\":\"4302\",\"code\":1,\"type\":\"error\"}";

        JSONObject json = JSONObject.parseObject(message);

        //JSONObject body = jsonObject.get("");

        if (json.get("type").toString().equals("error") && json.get("code").toString().equals("1")) {

            System.out.println(111);
            //return ApiResponseResult.build(2012, "error", "币种不存在", "");
        }
        if (json.get("type").toString().equals("error") && !json.get("code").toString().equals("1")) {

            System.out.println(222);
            //return ApiResponseResult.build(2012, "error", "系统异常", "");
        }


    }



}

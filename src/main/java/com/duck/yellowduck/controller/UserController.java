package com.duck.yellowduck.controller;

import com.duck.yellowduck.domain.model.model.User;
import com.duck.yellowduck.domain.model.response.ApiResponseResult;
import com.duck.yellowduck.domain.service.UserService;
import com.duck.yellowduck.publics.MnemonitUtitls;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(value="用户业务接口", tags={"user service"})
public class UserController
{
    @Autowired
    private UserService userService;                //用户Service

    @ApiOperation(value="同步用户信息", notes="同步信息")
    @ApiImplicitParam(name="user", value="用户对象user", dataType="User")
    @RequestMapping(value={"/synchronousUserInfo"}, method= RequestMethod.POST)
    public ApiResponseResult synchronousUserInfo(@RequestBody User user)
    {
        ApiResponseResult apiResponseResult = new ApiResponseResult();
        try
        {
            apiResponseResult = this.userService.synchronousUserInfo(user);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ApiResponseResult.build(Integer.valueOf(2010), "error", "出现异常", "");
        }
        return apiResponseResult;
    }

    @ApiOperation(value="查询当前用户信息", notes="按 手机号(phone)查询当前用户信息")
    @ApiImplicitParam(name="phone", value="������", dataType="String", paramType="query", required=true)
    @RequestMapping(value={"/queryUserByPhone"}, method=RequestMethod.GET)
    public ApiResponseResult queryUserByPhone(@RequestParam("phone") String phone){

        ApiResponseResult apiResponseResult = new ApiResponseResult();
        try
        {
            apiResponseResult = this.userService.queryUserByPhone(phone);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ApiResponseResult.build(Integer.valueOf(2010), "error", "出现异常", "");
        }
        return apiResponseResult;
    }



    @ApiOperation(value = "根据手机号码生成助记词",notes = "按照手机号码生成助记词")
    @ApiImplicitParam(name="phone", value="18129851846", dataType="String", paramType="query", required=true)
    @RequestMapping(value={"/keyWorld"}, method=RequestMethod.GET)
    public ApiResponseResult synchronousUserKeyWokld(@RequestParam("phone")String phone){
        User user = new User();
        user.setPhone(phone);
        ApiResponseResult apiResponseResult = null;
        try
        {

            String keyWorld = MnemonitUtitls.generateMnemonic();
            user.setMemorizingWords(keyWorld);
            //同步用户信息成功
            this.userService.synchronousUserInfo(user);
            //apiResponseResult = this.userService.synchronousUserInfo(user);
            apiResponseResult = ApiResponseResult.build(200,"succss","获取助记词成功",keyWorld);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return ApiResponseResult.build(Integer.valueOf(2010), "error", "出现异常", "");
        }
        return apiResponseResult;
    }

}

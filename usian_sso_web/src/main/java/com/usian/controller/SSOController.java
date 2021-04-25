package com.usian.controller;


import com.usian.feigh.SSOSeriviceFeign;
import com.usian.pojo.TbUser;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("frontend/sso")
public class SSOController {

    @Autowired
    private SSOSeriviceFeign ssoSeriviceFeign;

    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag){
        Boolean checkUserInfo= ssoSeriviceFeign.checkUserInfo(checkValue,checkFlag);
        if(checkUserInfo){
            return Result.ok(checkUserInfo);
        }
        return Result.error("校验失败");
    }
    @RequestMapping("/userRegister")
    public Result userRegister(TbUser user){
        Integer userRegister = ssoSeriviceFeign.userRegister(user);
        if(userRegister==1){
            return Result.ok();
        }
        return Result.error("注册失败");
    }
    @RequestMapping("/userLogin")
    public Result userLogin(String username,String password){

        Map map = ssoSeriviceFeign.userLogin(username, password);
        if(map!=null){
            return Result.ok(map);
        }
        return Result.error("登录失败");
    }
    @RequestMapping("/getUserByToken/{token}")
    @ResponseBody
    public Result getUserByToken(@PathVariable String token) {
        TbUser tbUser = ssoSeriviceFeign.getUserByToken(token);
        if(tbUser!=null){
            return Result.ok();
        }
        return Result.error("登录过期");
    }
    @RequestMapping("/logOut")
    public Result logOut(String token){
        Boolean logOut = ssoSeriviceFeign.logOut(token);
        if(logOut){
            return Result.ok();
        }
        return Result.error("退出失败");
    }
}
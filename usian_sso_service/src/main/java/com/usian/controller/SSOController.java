package com.usian.controller;

import com.usian.pojo.TbUser;
import com.usian.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户注册与登录
 */
@RestController
@RequestMapping("/service/sso")
public class SSOController {
    @Autowired
    private SSOService ssoService;

    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(String checkValue, Integer checkFlag) {
        return this.ssoService.checkUserInfo(checkValue, checkFlag);
    }
    @RequestMapping("/userRegister")
    public Integer userRegister(@RequestBody TbUser user) {
        return this.ssoService.userRegister(user);
    }
    @RequestMapping("/userLogin")
    public Map userLogin(String username, String password) {
        return this.ssoService.userLogin(username, password);
    }
    @RequestMapping("/getUserByToken/{token}")
    @ResponseBody
    public TbUser getUserByToken(@PathVariable String token) {
        return ssoService.getUserByToken(token);
    }
    @RequestMapping("/logOut")
    public Boolean logOut(String token){
        return this.ssoService.logOut(token);
    }
}
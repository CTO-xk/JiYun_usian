package com.usian.service;

import com.usian.config.RedisClient;
import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.pojo.TbUserExample;

import com.usian.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SSOService {
    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${SESSION_EXPIRE}")
    private  Long SESSION_EXPIRE;

    @Value("${USER_INFO}")
    private  String USER_INFO;


    public boolean checkUserInfo(String checkValue, Integer checkFlag) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if (checkFlag == 1) {
            criteria.andUsernameEqualTo(checkValue);
        } else if (checkFlag == 2) {
            criteria.andPhoneEqualTo(checkValue);
        }
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list == null || list.size() == 0) {

            return true;
        }
        return false;
    }

    public Integer userRegister(TbUser user) {
        String pwd = MD5Utils.digest(user.getPassword());
        user.setPassword(pwd);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        return this.tbUserMapper.insert(user);
    }

    public Map userLogin(String username, String password) {
        // 1、判断用户名密码是否正确。
        String pwd = MD5Utils.digest(password);
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(pwd);
        List<TbUser> userList = this.tbUserMapper.selectByExample(example);
        if(userList == null || userList.size() <= 0){
            return null;
        }
        TbUser tbUser = userList.get(0);
        String token = UUID.randomUUID().toString();
        tbUser.setPassword(null);
        redisClient.set(USER_INFO + ":" + token, tbUser);
        redisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
        Map<String,String> map = new HashMap<String,String>();
        map.put("token",token);
        map.put("userid",tbUser.getId().toString());
        map.put("username",tbUser.getUsername());
        return map;
    }

    public TbUser getUserByToken(String token) {
        TbUser tbUser = (TbUser) redisClient.get(USER_INFO + ":" + token);
        if(tbUser!=null){
            redisClient.expire(USER_INFO+":"+token,SESSION_EXPIRE);
            return tbUser;
        }
        return null;
    }

    public Boolean logOut(String token) {
        return redisClient.del(USER_INFO + ":" + token);
    }
}

package com.usian.service;

import com.usian.config.RedisClient;
import com.usian.pojo.TbItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {
    @Autowired
    private RedisClient redisClient;

    @Value("${CART_REDIS_KEY}")
    private String CART_REDIS_KEY;


    public Map<String, TbItem> selectCartByUserId(String userId) {
        return (Map<String, TbItem>) redisClient.hget(CART_REDIS_KEY,userId);
    }


    public Boolean insertCart(String userId, Map<String, TbItem> cart) {
        return redisClient.hset(CART_REDIS_KEY, userId, cart);
    }
}

package com.usian.service;


import com.jiyun.utils.CatNode;
import com.jiyun.utils.CatResult;
import com.usian.config.RedisClient;
import com.usian.mapper.TbItemCatMapper;
import com.usian.pojo.TbItemCat;
import com.usian.pojo.TbItemCatExample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCategoryService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Value("${PROTAL_CATRESULT_KEY}")
    private String portal_catresult_redis_key;

    @Autowired
    private RedisClient redisClient;

    public List<TbItemCat> selectItemCategoryByParentId(Long id) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(1);
        criteria.andParentIdEqualTo(id);
        return tbItemCatMapper.selectByExample(example);
    }

    public CatResult selectItemCategoryAll() {
        CatResult catResultRedis = (CatResult) redisClient.get(portal_catresult_redis_key);
        if(catResultRedis!=null){
            return catResultRedis;
        }
        CatResult catResult = new CatResult();
        catResult.setData(getCatList(0L));
        redisClient.set(portal_catresult_redis_key, catResult);
        return catResult;
    }
    private List<?> getCatList(Long parentId){
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = this.tbItemCatMapper.selectByExample(example);
        List resultList = new ArrayList();
        int count = 0;
        for(TbItemCat tbItemCat:list){
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                catNode.setName(tbItemCat.getName());
                catNode.setItem(getCatList(tbItemCat.getId()));
                resultList.add(catNode);
                count++;
                if (count == 18){
                    break;
                }
            }else{
                resultList.add(tbItemCat.getName());
            }
        }
        return resultList;
    }
}

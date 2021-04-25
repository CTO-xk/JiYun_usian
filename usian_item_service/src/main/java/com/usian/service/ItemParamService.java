package com.usian.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.config.RedisClient;
import com.usian.mapper.TbItemParamItemMapper;
import com.usian.mapper.TbItemParamMapper;
import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamExample;
import com.usian.pojo.TbItemParamItem;
import com.usian.pojo.TbItemParamItemExample;

import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemParamService {

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${PARAM}")
    private String PARAM;

    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;

    @Value("${SETNX_PARAM_LOCK_KEY}")
    private String SETNX_PARAM_LOCK_KEY;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(itemCatId);
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if(tbItemParams != null && tbItemParams.size() > 0){
            return tbItemParams.get(0);
        }
        return null;
    }

    public PageResult selectItemParamAll(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);

        List<TbItemParam> itemParamList = tbItemParamMapper.selectByExampleWithBLOBs(null);

        PageInfo<TbItemParam> pageInfo = new PageInfo<>(itemParamList);
        PageResult pageResult = new PageResult();
        pageResult.setResult(pageInfo.getList());
        pageResult.setTotalPage(Long.valueOf(pageInfo.getPages()));
        pageResult.setTotalPage(pageInfo.getTotal());
        return pageResult;
    }
    public Integer insertItemParam(TbItemParam tbItemParam) {
        tbItemParam.setCreated(new Date());
        tbItemParam.setUpdated(new Date());
        return tbItemParamMapper.insertSelective(tbItemParam);
    }

    public Integer deleteItemParamById(Long id) {
        Integer num=tbItemParamMapper.deleteByPrimaryKey(id);
        return num;
    }
    public TbItemParamItem selectTbItemParamItemByItemId(Long itemId) {
        //1、先查询redis,如果有直接返回
        TbItemParamItem tbItemParamItem = (TbItemParamItem) redisClient.get(ITEM_INFO +
                ":" + itemId + ":" + PARAM);
        if(tbItemParamItem!=null){
            return tbItemParamItem;
        }
        if(redisClient.setnx(SETNX_PARAM_LOCK_KEY+":"+itemId,itemId,30L)){
            //2、再查询mysql,并把查询结果缓存到redis,并设置失效时间
            TbItemParamItemExample tbItemParamItemExample = new TbItemParamItemExample();
            TbItemParamItemExample.Criteria criteria =
                    tbItemParamItemExample.createCriteria();
            criteria.andItemIdEqualTo(itemId);
            List<TbItemParamItem> tbItemParamItems =
                    tbItemParamItemMapper.selectByExampleWithBLOBs(tbItemParamItemExample);
            if(tbItemParamItems!=null && tbItemParamItems.size()>0){
                tbItemParamItem = tbItemParamItems.get(0);
                redisClient.set(ITEM_INFO + ":" + itemId + ":" + PARAM,tbItemParamItem);
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" +
                        PARAM,ITEM_INFO_EXPIRE);

            }else{
                redisClient.set(ITEM_INFO + ":" + itemId + ":" + PARAM,null);
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + PARAM,30L);
            }
            redisClient.del(SETNX_PARAM_LOCK_KEY+":"+itemId);
            return  tbItemParamItem;
        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return selectTbItemParamItemByItemId(itemId);
        }
    }
}

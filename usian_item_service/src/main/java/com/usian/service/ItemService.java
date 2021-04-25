package com.usian.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.usian.config.RedisClient;
import com.usian.mapper.*;
import com.usian.pojo.*;

import com.usian.utils.IDUtils;
import com.usian.utils.PageResult;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    @Value("${BASE}")
    private String BASE;

    @Value("${DESC}")
    private String DESC;

    @Value("${ITEM_INFO_EXPIRE}")
    private Long ITEM_INFO_EXPIRE;

    @Value("${SETNX_BASC_LOCK_KEY}")
    private String SETNX_BASC_LOCK_KEY;

    @Value("${SETNX_DESC_LOCK_KEY}")
    private String SETNX_DESC_LOCK_KEY;


    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    public TbItem getById(Long itemId) {
        return tbItemMapper.selectByPrimaryKey(itemId);
    }

    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        TbItemExample example = new TbItemExample();
        example.setOrderByClause("updated desc");
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo((byte)1);
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(tbItems);
        return new PageResult(page, tbItemPageInfo.getTotal(), tbItemPageInfo.getList());
    }

    public Integer insertTbItem(TbItem tbItem, String desc, String itemParams) {
        Date date = new Date();
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        tbItem.setStatus((byte)1);

        int num1 = tbItemMapper.insert(tbItem);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int num2 = tbItemDescMapper.insert(tbItemDesc);
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);
        int num3 = tbItemParamItemMapper.insert(tbItemParamItem);
        //发布消息到mq,同步索引库
        amqpTemplate.convertAndSend("item_exchage","item.add", itemId);

        return num1+num2+num3;
    }

    public Map<String, Object> preUpdateItem(Long itemId) {
        Map<String, Object> map = new HashMap<>();
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
        map.put("item", tbItem);
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
        map.put("itemDesc", tbItemDesc.getItemDesc());
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if(tbItemParamItems != null && tbItemParamItems.size() > 0){
            map.put("itemParamItem", tbItemParamItems.get(0).getParamData());
        }
        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItem.getCid());
        map.put("itemCat", tbItemCat.getName());
        return map;
    }

    public Integer deleteItemById(Long itemId) {
        TbItem tbItem = new TbItem();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 3);
        Integer num=tbItemMapper.updateByPrimaryKeySelective(tbItem);
        redisClient.del(String.valueOf(itemId));
        return num;
    }

    public Integer updateTbItemByOrderId(String orderId) {
        TbOrderItemExample tbOrderItemExample = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = tbOrderItemExample.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<TbOrderItem> tbOrderItemList =tbOrderItemMapper.selectByExample(tbOrderItemExample);
        int result = 0;
        for (int i = 0; i < tbOrderItemList.size(); i++) {
            TbOrderItem tbOrderItem =  tbOrderItemList.get(i);
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(Long.valueOf(tbOrderItem.getItemId()));
            tbItem.setNum(tbItem.getNum()-tbOrderItem.getNum());
            result += tbItemMapper.updateByPrimaryKeySelective(tbItem);
        }
        return result;
    }

    public TbItem selectItemInfo(Long itemId){
        TbItem tbItem = (TbItem) redisClient.get(ITEM_INFO+":"+itemId+":"+BASE);
        if(tbItem!=null){
            return tbItem;
        }

        if(redisClient.setnx(SETNX_BASC_LOCK_KEY+":"+itemId,itemId,30L)){
            //2、再查询mysql,并把查询结果缓存到redis,并设置失效时间
            tbItem = tbItemMapper.selectByPrimaryKey(itemId);
            if(tbItem!=null){
                redisClient.set(ITEM_INFO+":"+itemId+":"+BASE,tbItem);
                redisClient.expire(ITEM_INFO+":"+itemId+":"+BASE,ITEM_INFO_EXPIRE);
            }else{
                redisClient.set(ITEM_INFO+":"+itemId+":"+BASE,null);
                redisClient.expire(ITEM_INFO+":"+itemId+":"+BASE,30L);
            }
            redisClient.del(SETNX_BASC_LOCK_KEY+":"+itemId);
            return tbItem;
        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return selectItemInfo(itemId);
        }
    }

    public TbItemDesc selectItemDescByItemId(Long itemId) {
        TbItemDesc tbItemDesc = (TbItemDesc) redisClient.get(ITEM_INFO + ":" + itemId + ":" + DESC);
        if(tbItemDesc!=null){
            return tbItemDesc;
        }
        if(redisClient.setnx(SETNX_DESC_LOCK_KEY+":"+itemId,itemId,30L)){
            tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

            if(tbItemDesc!=null){
                redisClient.set(ITEM_INFO + ":" + itemId + ":" + DESC,tbItemDesc);
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + DESC,ITEM_INFO_EXPIRE);
            }else{
                redisClient.set(ITEM_INFO + ":" + itemId + ":" + DESC,null);
                redisClient.expire(ITEM_INFO + ":" + itemId + ":" + DESC,30L);
            }
            redisClient.del(SETNX_DESC_LOCK_KEY+":"+itemId);
            return tbItemDesc;
        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return selectItemDescByItemId(itemId);
        }
    }

    public Integer updateTbItem(TbItem tbItem, String desc, String itemParams) {
        Date date = new Date();
        Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        tbItem.setStatus((byte)1);

        int num1 = tbItemMapper.updateByPrimaryKeySelective(tbItem);
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(date);
        tbItemDesc.setUpdated(date);
        int num2 = tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);
        int num3 = tbItemParamItemMapper.updateByPrimaryKeySelective(tbItemParamItem);
        return num1+num2+num3;
    }
}

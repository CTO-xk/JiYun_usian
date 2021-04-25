package com.usian.controller;

import com.usian.utils.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/detail")
public class DetailController {
    @Autowired
    private ItemServiceFeign itemServiceFeign;

    /**
     * 查询商品基本信息
     */
    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(Long itemId) {
        TbItem tbItem = itemServiceFeign.selectItemInfo(itemId);
        if (tbItem != null) {
            return Result.ok(tbItem);
        }
        return Result.error("查无结果");
    }

    /**
     * 查询商品介绍
     */
    @RequestMapping("/selectItemDescByItemId")
    public Result selectItemDescByItemId(Long itemId){
        TbItemDesc tbItemDesc = itemServiceFeign.selectItemDescByItemId(itemId);
        if(tbItemDesc != null){
            return Result. ok (tbItemDesc);
        }
        return Result.error ("查无结果");
    }

    /**
     * 根据商品 ID 查询商品规格参数
     */
    @RequestMapping("/selectTbItemParamItemByItemId")
    public Result selectTbItemParamItemByItemId(Long itemId){
        TbItemParamItem tbItemParamItem =
                itemServiceFeign.selectTbItemParamItemByItemId(itemId);
        if(tbItemParamItem != null){
            return Result.ok(tbItemParamItem);
        }
        return Result. error ("查无结果");
    }
}
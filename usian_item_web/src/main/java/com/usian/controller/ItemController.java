package com.usian.controller;

import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("backend/item")
public class ItemController {


    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping(" ")
    public Result selectItemInfo(Long itemId){
        TbItem tbItem=itemServiceFeign.selectItemInfo(itemId);
        if (tbItem != null){
            return  Result.ok(tbItem);
        }
        return Result.error("查不到");
    }

    @RequestMapping("selectTbItemAllByPage")
    public  Result selectTbItemAllByPage(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "3") Integer rows){
        PageResult pageResult=itemServiceFeign.selectTbItemAllByPage(page,rows);
        if (pageResult.getResult() != null && pageResult.getResult().size()>0){
            return  Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }

    @RequestMapping("insertTbItem")
    public Result insertTbItem(TbItem tbItem,String desc,String itemParams){
        Integer insertTbItemNum = itemServiceFeign.insertTbItem(tbItem, desc,itemParams);
        if(insertTbItemNum==3){
            return Result.ok();
        }
        return Result.error("添加失败了");

    }
    @RequestMapping("deleteItemById")
    public Result deleteItemById(Long itemId){
        Integer insertTbItemNum = itemServiceFeign.deleteItemById(itemId);
        if(insertTbItemNum == 1){
            return Result.ok();
        }
        return Result.error("删除失败");
    }

    @RequestMapping("preUpdateItem")
    public Result preUpdateItem(Long itemId){
        Map<String, Object> map = itemServiceFeign.preUpdateItem(itemId);
        if(map != null){
            return Result.ok(map);
        }
        return Result.error("查询不出来");
    }


}

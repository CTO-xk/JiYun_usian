package com.usian.controller;

import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backend/itemParam")
public class ItemParamController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("selectItemParamByItemCatId/{itemCatId}")
    public Result selectItemParamByItemCatId(@PathVariable Long itemCatId){
        TbItemParam tbItemParam = itemServiceFeign.selectItemParamByItemCatId(itemCatId);
        if(tbItemParam != null){
            return Result.ok(tbItemParam);
        }
        return Result.error("查无结果");
    }

    @RequestMapping("selectItemParamAll")
    public Result selectItemParamAll(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "3")Integer rows){
        PageResult pageResult=itemServiceFeign.selectItemParamAll(page,rows);
        if (pageResult.getResult().size()>0){
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }

    @RequestMapping("insertItemParam")
    public Result insertItemParam(Long itemCatId,String paramData){
        Integer num=itemServiceFeign.insertItemParam(itemCatId,paramData);
        if (num == 1){
            return Result.ok();
        }
        return  Result.error("添加失败，已有");
    }

    @RequestMapping("deleteItemParamById")
    public Result deleteItemParamById(Long id){
        Integer num=itemServiceFeign.deleteItemParamById(id);
        if (num == 1){
            return Result.ok();
        }
        return Result.error("删除失败");
    }




}

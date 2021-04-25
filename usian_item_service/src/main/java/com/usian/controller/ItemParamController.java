package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.pojo.TbItemParamItem;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("service/itemParam")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    @RequestMapping("selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable Long itemCatId){
        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }

    @RequestMapping("selectItemParamAll")
    public PageResult selectItemParamAll(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "5") Integer rows){
        return itemParamService.selectItemParamAll(page,rows);
    }

    @RequestMapping("insertItemParam")
    public Integer insertItemParam(@RequestBody TbItemParam tbItemParam){
        return itemParamService.insertItemParam(tbItemParam);
    }
    @RequestMapping("deleteItemParamById")
    public Integer deleteItemParamById( Long id){
        return itemParamService.deleteItemParamById(id);
    }

    @RequestMapping("/selectTbItemParamItemByItemId")
    public TbItemParamItem selectTbItemParamItemByItemId(@RequestParam Long itemId){
        return itemParamService.selectTbItemParamItemByItemId(itemId);
    }
}

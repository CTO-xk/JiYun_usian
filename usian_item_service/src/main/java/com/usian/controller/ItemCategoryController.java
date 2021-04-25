package com.usian.controller;


import com.jiyun.utils.CatResult;
import com.usian.pojo.TbItemCat;
import com.usian.service.ItemCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("service/itemCategory")
public class ItemCategoryController {

    @Autowired
    private ItemCategoryService itemCategoryService;

    @RequestMapping("selectItemCategoryByParentId")
    public List<TbItemCat> selectItemCategoryByParentId(Long id){
        return itemCategoryService.selectItemCategoryByParentId(id);
    }
    @RequestMapping("selectItemCategoryAll")
   public CatResult selectItemCategoryAll(){
        return itemCategoryService.selectItemCategoryAll();
    }
}

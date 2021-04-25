package com.usian.controller;

import com.usian.utils.Result;
import com.usian.feign.ItemServiceFeign;
import com.usian.pojo.TbItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("backend/itemCategory")
public class ItemCategoryController {

    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(defaultValue = "0") Long id){
        List<TbItemCat> list=itemServiceFeign.selectItemCategoryByParentId(id);
        if (list != null && list.size()>0){
           return   Result.ok(list);
        }
        return Result.error("查询失败");
    }
}

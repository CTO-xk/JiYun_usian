package com.usian.controller;



import com.jiyun.utils.CatResult;
import com.usian.utils.Result;
import com.usian.feign.ItemServiceFeign;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("frontend/itemCategory")
public class ItemCategoryController {
    @Autowired
    private ItemServiceFeign itemServiceFeign;

    @RequestMapping("selectItemCategoryAll")
    public Result selectItemCategoryAll() {
        CatResult catResult=itemServiceFeign.selectItemCategoryAll();
        if(catResult.getData().size()>0){
            return Result.ok(catResult);
        }
        return Result.error("查无结果");
    }
}

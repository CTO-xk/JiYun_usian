package com.usian.controller;

import com.usian.pojo.SearchItem;
import com.usian.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("service/search")
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("importAll")
    public Boolean importAll(){
        return searchItemService.importAll();
    }

    @RequestMapping("list")
    public List<SearchItem> list(String q, @RequestParam(defaultValue = "1")
            Long page, @RequestParam(defaultValue = "20") Integer pageSize){
        return searchItemService.list(q, page, pageSize);
    }
}

package com.usian.controller;



import com.jiyun.utils.AdNode;
import com.usian.pojo.TbContent;
import com.usian.service.ContentService;

import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("service/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/selectTbContentAllByCategoryId")
    public PageResult selectTbContentAllByCategoryId(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam Long categoryId) {
        return contentService.selectTbContentAllByCategoryId(page, rows, categoryId);
    }

    @RequestMapping("/insertTbContent")
    public Integer insertTbContent(@RequestBody TbContent tbContent){
        return contentService.insertTbContent(tbContent);
    }

    @RequestMapping("deleteContentByIds")
    public Integer deleteContentByIds(@RequestParam("id") Long id){
        return contentService.deleteContentByIds(id);
    }
    @RequestMapping("selectFrontendContentByAD")
    public List<AdNode> selectFrontendContentByAD(){
        return contentService.selectFrontendContentByAD();
    }
}

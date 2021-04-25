package com.usian.feign;


import com.jiyun.utils.AdNode;
import com.usian.utils.PageResult;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "usian-content-service")
public interface ContentServiceFeign {

    @RequestMapping("service/contentCategory/selectContentCategoryByParentId")
    List<TbContentCategory> selectContentCategoryByParentId(@RequestParam("parentId") Long id);
    @PostMapping("/service/contentCategory/insertContentCategory")
    Integer insertContentCategory(TbContentCategory tbContentCategory);
    @PostMapping("/service/contentCategory/deleteContentCategoryById")
    Integer deleteContentCategoryById(@RequestParam("categoryId") Long categoryId);

    @PostMapping("/service/content/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(@RequestParam("page") Integer page, @RequestParam("rows") Integer rows, @RequestParam("categoryId") Long categoryId);

    @PostMapping("/service/content/insertTbContent")
    Integer insertTbContent(TbContent tbContent);
    @RequestMapping("/service/content/deleteContentByIds")
    Integer deleteContentByIds(@RequestParam("id") Long id);
    @PostMapping("/service/content/selectFrontendContentByAD")
    List<AdNode> selectFrontendContentByAD();

}

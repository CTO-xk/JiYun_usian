package com.usian.feign;

import com.jiyun.utils.CatResult;
import com.usian.pojo.*;

import com.usian.utils.PageResult;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient("usian-item-service")
public interface ItemServiceFeign {

    @RequestMapping("service/item/selectItemInfo")
    TbItem selectItemInfo(@RequestParam Long itemId);

    @GetMapping("/service/item/selectTbItemAllByPage")
    PageResult selectTbItemAllByPage(@RequestParam Integer page, @RequestParam Integer rows);

    @GetMapping("/service/itemCategory/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam Long id);

    @GetMapping("/service/item/insertTbItem")
    Integer insertTbItem(@RequestBody TbItem tbItem, @RequestParam String desc, @RequestParam String itemParams);


    @RequestMapping("/service/itemParam/selectItemParamByItemCatId/{itemCatId}")
    TbItemParam selectItemParamByItemCatId(@PathVariable Long itemCatId);
    @GetMapping("/service/item/deleteItemById")
    Integer deleteItemById(@RequestParam Long itemId);
    @RequestMapping("service/item/preUpdateItem")
    Map<String, Object> preUpdateItem(@RequestParam Long itemId);
    @RequestMapping("/service/itemParam/selectItemParamAll")
    PageResult selectItemParamAll(@RequestParam Integer page,@RequestParam Integer rows);

    @RequestMapping("/service/itemParam/insertItemParam")
    Integer insertItemParam(@RequestParam Long itemCatId,@RequestParam String paramData);
    @RequestMapping("/service/itemParam/deleteItemParamById")
    Integer deleteItemParamById(@RequestParam Long id);
    @GetMapping("/service/itemCategory/selectItemCategoryAll")
    CatResult selectItemCategoryAll();

    @GetMapping("service/item/selectItemDescByItemId")
    TbItemDesc selectItemDescByItemId(@RequestParam("itemId")Long itemId);
    @GetMapping("service/itemParam/selectTbItemParamItemByItemId")
    TbItemParamItem selectTbItemParamItemByItemId(@RequestParam Long itemId);
}

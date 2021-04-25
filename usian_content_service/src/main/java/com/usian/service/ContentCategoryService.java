package com.usian.service;

import com.usian.mapper.TbContentCategoryMapper;
import com.usian.pojo.TbContentCategory;
import com.usian.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    public List<TbContentCategory> selectContentCategoryByParentId(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list=tbContentCategoryMapper.selectByExample(example);
        return list;


    }

    public Integer insertContentCategory(TbContentCategory tbContentCategory) {
        //1、添加内容分类
        tbContentCategory.setUpdated(new Date());
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setIsParent(false);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setStatus(1);
        Integer contentCategoryNum = tbContentCategoryMapper.insert(tbContentCategory);
        //2、如果他爹不是爹，要把他爹改成爹
        //2.1、查询当前新节点的父节点
        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
        //2.2、判断当前父节点是否是叶子节点
        if (!contentCategory.getIsParent()) {
            contentCategory.setIsParent(true);
            contentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKey(contentCategory);
        }
        return contentCategoryNum;
    }

    public Integer deleteContentCategoryById(Long categoryId) {

        TbContentCategory tbContentCategory =tbContentCategoryMapper.selectByPrimaryKey(categoryId);

        if(tbContentCategory.getIsParent()==true){
            return 0;
        }

        tbContentCategoryMapper.deleteByPrimaryKey(categoryId);
        TbContentCategoryExample tbContentCategoryExample = new
                TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria =tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(tbContentCategory.getParentId());
        List<TbContentCategory> tbContentCategoryList =  tbContentCategoryMapper.selectByExample(tbContentCategoryExample);

        if(tbContentCategoryList.size()==0){
            TbContentCategory parenttbContentCategory = new TbContentCategory();
            parenttbContentCategory.setId(tbContentCategory.getParentId());
            parenttbContentCategory.setIsParent(false);
            parenttbContentCategory.setUpdated(new Date());
            this.tbContentCategoryMapper.updateByPrimaryKeySelective(
                    parenttbContentCategory);
        }
        return 200;
    }
}

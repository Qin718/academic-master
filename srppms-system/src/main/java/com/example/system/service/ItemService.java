package com.example.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.ProjectBo;
import com.example.system.domain.entity.Item;
import com.example.system.domain.vo.PageVo;

import java.util.List;

/**
 * 项目日志(SysItem)表服务接口
 */
public interface ItemService extends IService<Item> {
    PageVo getPageVo(PageBo pageBo);

    List<Item> getList();

    void createItemRedis();

    R<PageVo> getPageVoSearch(ProjectBo projectBo, PageBo pageBo);

    String insertItemBo(ProjectBo projectBo);

    String deleteItem(String projectId, String itemId);

    PageVo getPageVoMyItem(PageBo pageBo);

    R<PageVo> getPageVoMyItemSearch(ProjectBo projectBo, PageBo pageBo);

    PageVo getPageVoProcess(PageBo pageBo);

    R<PageVo> getPageVoProcessSearch(PageBo pageBo, ProjectBo projectBo);

    void updateItemsRedis();

    String updateItem(ProjectBo projectBo);

    String deleteItem(List<Integer> list);
}


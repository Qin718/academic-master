package com.example.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.model.Interface;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.InterfaceMapper;
import com.example.system.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface> implements InterfaceService {
    @Autowired
    private InterfaceMapper interfaceMapper;

    /**
     * 获取接口信息列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<Interface> list = interfaceMapper.getInterfaceList();
        return new PageVo(pageBo, list);
    }
}

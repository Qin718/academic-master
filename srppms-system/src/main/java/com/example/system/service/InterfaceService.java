package com.example.system.service;


import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;

public interface InterfaceService {
    PageVo getPageVo(PageBo pageBo);
}

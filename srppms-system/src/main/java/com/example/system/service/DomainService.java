package com.example.system.service;


import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import lombok.SneakyThrows;

public interface DomainService {
    @SneakyThrows
    PageVo getPageVo(PageBo pageBo);
}

package com.example.generator.service;

import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;

public interface GenService {
    R<PageVo> getList(PageBo pageBo);

    R<String> create(String table);
}

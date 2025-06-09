package com.example.system.service;


import com.example.common.response.R;
import com.example.system.domain.bo.LogBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Log;
import com.example.system.domain.vo.PageVo;

import java.util.List;

public interface LogService {
    PageVo getPageVo(PageBo pageBo);

    R<PageVo> getPageVoSearch(LogBo bo, PageBo pageBo);

    void insertLog(Log log);

    String deleteLog(List<Integer> list);
}

package com.example.system.service;

import com.example.common.response.R;
import com.example.system.domain.bo.ConfigBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Config;
import com.example.system.domain.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface ConfigService {

    PageVo getPageVo(PageBo pageBo);

    String insertConfig(Config config);

    String deleteConfig(List<Integer> list);

    String updateConfig(Config config);

    Map<String, List<Config>> getMap();

    R<PageVo> getPageVoBySearch(ConfigBo configBo, PageBo pageBo);
}

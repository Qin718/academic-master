package com.example.system.service;


import com.example.system.domain.vo.DictVo;

import java.util.List;

public interface DictService {

    List<DictVo> getDictVoList(String dict);
}

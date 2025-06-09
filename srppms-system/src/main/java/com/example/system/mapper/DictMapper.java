package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.Dict;
import com.example.system.domain.vo.DictVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictMapper extends BaseMapper<Dict> {
    List<DictVo> getDictVoList();
}

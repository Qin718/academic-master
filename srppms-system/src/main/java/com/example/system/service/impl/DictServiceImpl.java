package com.example.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.entity.Dict;
import com.example.system.domain.vo.DictVo;
import com.example.system.mapper.DictMapper;
import com.example.system.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取字典列表
     */
    @Override
    public List<DictVo> getDictVoList(String dict) {
        List<DictVo> list = new ArrayList<>();
        if (StringUtils.isEmpty(dict)) {
            return list;
        }
        if ("swagger".equals(dict)) {
            String key = RedisConstant.DICT + "swagger";
            String url = redisUtils.get(key).toString();
            DictVo dictVo = new DictVo();
            dictVo.setDict(url);
            list.add(dictVo);
            return list;
        }

        List<DictVo> dictList = this.getDictVoList();
        list = dictList.stream().filter(o -> dict.equals(o.getType())).collect(Collectors.toList());
        return list;
    }

    public List<DictVo> getDictVoList() {
        return dictMapper.getDictVoList();
    }
}

package com.example.system.service;

import com.example.system.domain.vo.UpdateVo;

import java.util.List;

public interface UpdateService {

    List<UpdateVo> getUpdateList();

    void createUpdateRedis();
}

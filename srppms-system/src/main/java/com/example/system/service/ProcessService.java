package com.example.system.service;

import com.example.system.domain.bo.ProcessBo;

import java.util.List;
import java.util.Map;

public interface ProcessService {
    List<Integer> getListShareUser(Integer projectId);

    List<Map<String, Object>> getListAllShareUser();

    String changeUp(List<Integer> userIdS, Integer projectId);

    void createShareUserRedis();

    String updateProcess(ProcessBo processBo);
}

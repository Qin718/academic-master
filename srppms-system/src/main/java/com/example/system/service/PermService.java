package com.example.system.service;


import com.example.common.response.R;
import com.example.system.domain.entity.Perm;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface PermService {
    List<Perm> getList();

    void createPermRedis();

    List<String> getListByRole(Integer roleId);

    @Async
    void createUserPermList(String account);

    R<String> changeRolePerm(List<String> newPerm, List<String> oldPerm, Integer roleId);
}

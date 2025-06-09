package com.example.system.service;

import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.RoleBo;
import com.example.system.domain.entity.Role;
import com.example.system.domain.vo.PageVo;

import java.util.List;

public interface RoleService {

    PageVo getPageVo(PageBo pageBo);

    List<Role> getListRoleKind();

    void createRoleRedis();

    String insertRole(RoleBo roleBo);

    R<PageVo> getPageVoSearch(RoleBo roleBo, PageBo pageBo);

    String updateRole(RoleBo roleBo);

    String deleteRole(List<Integer> list);
}

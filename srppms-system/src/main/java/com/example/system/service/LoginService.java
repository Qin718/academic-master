package com.example.system.service;

import com.example.common.response.R;
import com.example.system.domain.bo.LoginBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.UserBo;
import com.example.system.domain.vo.LoginVo;
import com.example.system.domain.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface LoginService {
    R<Object> doLogin(LoginBo bo);

    R<Map<String, Object>> doRegister(UserBo bo);

    PageVo getPageVo(PageBo pageBo);

    List<LoginVo> getListIndex();

    Map<String, Object> virtualLogin(Integer id);
}

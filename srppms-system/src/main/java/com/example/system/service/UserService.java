package com.example.system.service;

import com.example.common.response.R;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.UserBo;
import com.example.system.domain.entity.User;
import com.example.system.domain.vo.PageVo;
import com.example.system.domain.vo.UserVo;

import java.util.List;
import java.util.Map;

public interface UserService {
    PageVo getPageVo(PageBo pageBo);

    List<UserVo> getList();

    R<PageVo> getPageVoSearch(UserBo userBo, PageBo pageBo);

    byte[] getUserImage(String account);

    Map<String, Object> getInfo();

    String changeStatus(UserBo bo);

    String deleteList(List<Integer> list);

    String insertUser(User user);

    R<String> updatePwd(UserBo bo);

    String updateUser(UserBo userBo);

    R<String> updateUserInfo(UserBo bo);

    PageVo getOnlineList(PageBo pageBo);

    void forcedOut(String account);

    R<String> importUser(List<String> paths);
}

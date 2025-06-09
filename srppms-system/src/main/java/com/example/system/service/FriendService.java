package com.example.system.service;


import com.example.common.response.R;
import com.example.system.domain.bo.FriendBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;

public interface FriendService {
    PageVo getPageVo(PageBo pageBo);

    R<PageVo> getPageVoBySearch(FriendBo friendBo, PageBo pageBo);

    PageVo getPageVoApp(PageBo pageBo);

    PageVo getPageVoAppLog(PageBo pageBo);

    String updateFriend(FriendBo friendBo);

    String deleteFriend(FriendBo bo);
}

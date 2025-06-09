package com.example.system.service;

import com.example.common.response.R;
import com.example.system.domain.bo.NoticesBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;

import java.util.List;

public interface NoticeService {
    void createNoticeRedis();

    PageVo getPageVo(PageBo pageBo);

    String insertNotice(NoticesBo noticesBo);

    R<PageVo> getPageVoSearch(NoticesBo noticesBo, PageBo pageBo);

    String updateNotice(NoticesBo noticesBo);

    String deleteNotice(List<Integer> list);
}

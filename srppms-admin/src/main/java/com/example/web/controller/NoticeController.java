package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.NoticesBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告操作
 **/
@RestController
@Api(value = "公告管理", tags = {"公告管理"})
@RequestMapping(value = "/notice", method = {RequestMethod.POST, RequestMethod.GET})
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @ResponseBody
    @ApiOperation("获取公告列表-分页")
    @RequestMapping("/getPageVo")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(noticeService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("搜索公告列表-分页")
    @RequestMapping("/getPageVo/search")
    public R<PageVo> getPageVoSearch(@MultiRequestBody NoticesBo noticesBo,
                                     @MultiRequestBody PageBo pageBo) {
        return noticeService.getPageVoSearch(noticesBo,pageBo);
    }

    @ResponseBody
    @ApiOperation("添加公告")
    @RequestMapping("/insert")
    public R<PageVo> insertNotice(@MultiRequestBody NoticesBo noticesBo,
                                  @MultiRequestBody PageBo pageBo) {
        return R.ok(noticeService.insertNotice(noticesBo), noticeService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("修改公告")
    @RequestMapping("/update")
    public R<PageVo> updateNotice(@MultiRequestBody NoticesBo noticesBo,
                                  @MultiRequestBody PageBo pageBo) {
        return R.ok(noticeService.updateNotice(noticesBo), noticeService.getPageVo(pageBo));
    }

    @ResponseBody
    @ApiOperation("删除公告")
    @RequestMapping("/delete")
    public R<PageVo> deleteNotice(@MultiRequestBody List<Integer> list,
                                  @MultiRequestBody PageBo pageBo) {
        return R.ok(noticeService.deleteNotice(list), noticeService.getPageVo(pageBo));
    }
}

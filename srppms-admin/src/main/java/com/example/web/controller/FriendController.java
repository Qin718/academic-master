package com.example.web.controller;

import com.example.common.basic.MultiRequestBody;
import com.example.common.response.R;
import com.example.system.domain.bo.FriendBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.vo.PageVo;
import com.example.system.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "好友管理", tags = {"好友管理"})
@RequestMapping(value = "/friend", method = {RequestMethod.POST, RequestMethod.GET})
public class FriendController {

    @Autowired
    private FriendService friendService;

    @ResponseBody
    @RequestMapping("/getPageVo")
    @ApiOperation("获取当前用户的好友列表-分页")
    public R<PageVo> getPageVo(@RequestBody PageBo pageBo) {
        return R.ok(friendService.getPageVo(pageBo));
    }

    @ResponseBody
    @RequestMapping("/getPageVo/search")
    @ApiOperation("搜索当前用户的好友列表-分页")
    public R<PageVo> getPageVoBySearch(@MultiRequestBody FriendBo friendBo,
                                       @MultiRequestBody PageBo pageBo) {
        return friendService.getPageVoBySearch(friendBo, pageBo);
    }

    @ResponseBody
    @RequestMapping("/getPageVo/App")
    @ApiOperation("获取当前用户的申请列表-分页")
    public R<PageVo> getPageVoApp(@RequestBody PageBo pageBo) {
        return R.ok(friendService.getPageVoApp(pageBo));
    }

    @ResponseBody
    @RequestMapping("/getPageVo/AppLog")
    @ApiOperation("当前用户发起的申请记录-分页")
    public R<PageVo> getPageVoAppLog(@RequestBody PageBo pageBo) {
        return R.ok(friendService.getPageVoAppLog(pageBo));
    }

    @ResponseBody
    @ApiOperation("更新好友")
    @RequestMapping("/update")
    public R<PageVo> updateFriend(@RequestBody FriendBo friendBo) {
        return R.ok(friendService.updateFriend(friendBo));
    }

    @ResponseBody
    @ApiOperation("删除好友")
    @RequestMapping("/delete")
    public R<PageVo> deleteFriend(@MultiRequestBody FriendBo friendBo, @MultiRequestBody PageBo pageBo) {
        return R.ok(friendService.deleteFriend(friendBo), friendService.getPageVo(pageBo));
    }
}

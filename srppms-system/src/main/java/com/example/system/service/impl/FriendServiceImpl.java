package com.example.system.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.SystemConstant;
import com.example.common.response.R;
import com.example.common.utils.GzipUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.FriendBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Friend;
import com.example.system.domain.vo.FriendVo;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.FriendMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GzipUtils gzipUtils;

    /**
     * 获取好友列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<FriendVo> list = this.getFriendList();
        list = list.stream().filter(o -> o.getStatus().equals(2)).collect(Collectors.toList());
        return new PageVo(pageBo, list);
    }

    /**
     * 获取好友列表-不分页
     */
    public List<FriendVo> getFriendList() {
        String account = (String) StpUtil.getLoginId();
        List<FriendVo> friendList = friendMapper.getFriendList(account);
        return friendList.stream().peek(this::imageToByte).collect(Collectors.toList());
    }

    /**
     * 搜索好友列表-分页
     */
    @Override
    public R<PageVo> getPageVoBySearch(FriendBo friendBo, PageBo pageBo) {
        List<FriendVo> list = this.getFriendList();
        String search = friendBo.getNotes();
        if (StringUtils.isEmpty(search)) {
            return R.ok(new PageVo(pageBo, list));
        }
        list = list.stream().filter(o ->
                (o.getNotes() != null && o.getNotes().contains(search)) ||
                        o.getName().contains(search) ||
                        o.getUserB().contains(search)
        ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(list)) {
            return R.ok(new PageVo(pageBo, list));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    /**
     * 当前用户的申请列表
     */
    @Override
    public PageVo getPageVoApp(PageBo pageBo) {
        int userId = this.getUserIdByAccount();
        List<FriendVo> friendList = friendMapper.getListApp(userId).stream().peek(this::imageToByte).collect(Collectors.toList());
        return new PageVo(pageBo, friendList);
    }

    /**
     * 当前用户发起的申请记录
     */
    @Override
    public PageVo getPageVoAppLog(PageBo pageBo) {
        //        int userId = this.getUserIdByAccount();
        //        List<FriendVo> list = friendMapper.getFriendList();
        //        list = list.stream().filter(o->o.getUserIdA().equals(userId)).collect(Collectors.toList());
        List<FriendVo> list = this.getFriendList();
        return new PageVo(pageBo, list);
    }

    /**
     * 好友备注 notes
     * 好友关系 status 2 同意 3 拒绝
     */
    @Override
    public String updateFriend(FriendBo bo) {
        LambdaQueryWrapper<Friend> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Friend::getUserIdA, bo.getUserIdA());
        lqw.eq(Friend::getUserIdB, bo.getUserIdB());
        Friend friend = friendMapper.selectOne(lqw);
        if(StringUtils.isNotEmpty(friend)) {
            friend.setStatus(bo.getStatus());
            friend.setNotes(bo.getNotes());
            friendMapper.updateById(friend);

            Integer friend_status_normal = SystemConstant.FRIEND_STATUS_NORMAL;
            if (bo.getStatus() - friend_status_normal == 0) {
                Friend f = new Friend();
                f.setUserA(bo.getUserB());
                f.setUserIdA(bo.getUserIdB());
                f.setUserB(bo.getUserA());
                f.setUserIdB(bo.getUserIdA());
                f.setStatus(friend_status_normal);
                f.setNotes("");
                friendMapper.insertFriend(f);
            }
            return "修改成功";
        }
        return "修改失败";
    }

    @Override
    public String deleteFriend(FriendBo bo) {
        friendMapper.deleteFriend(bo);
        return "删除成功";
    }

    /**
     * 通过账号找用户ID
     */
    public int getUserIdByAccount() {
        String account = (String) StpUtil.getLoginId();
        return userMapper.getUserByAccount(account).getId();
    }

    /**
     * 图片转二进制
     */
    public void imageToByte(FriendVo vo) {
        Map<String, byte[]> map = new HashMap<>();
        map.put("byte", gzipUtils.compress(vo.getImage()));
        vo.setImageByte(map);
        vo.setImage("");
    }
}

package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.common.webSocket.SendSocket;
import com.example.common.webSocket.WebSocket;
import com.example.system.domain.bo.NoticesBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Notices;
import com.example.system.domain.model.Notify;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.NoticeMapper;
import com.example.system.service.NoticeService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notices> implements NoticeService {
    final String key = RedisConstant.GET_LIST_NOTICE;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private WebSocket webSocket;


    /**
     * 创建系统公告缓存
     */
    @Override
    public void createNoticeRedis() {
        List<Notices> list = getNoticeList();
        long time = RedisConstant.GET_LIST_NOTICE_TIME;
        redisUtils.set(key, list, time);
    }

    private List<Notices> getNoticeList() {
        LambdaQueryWrapper<Notices> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Notices::getTitle).orderByAsc(Notices::getId);
        return noticeMapper.selectList(lqw);
    }

    /**
     * 获取系统公告列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<Notices> list = this.getNoticeList();
        return new PageVo(pageBo, list);
    }

    /**
     * 新增系统公告，并发送广播
     * @return R.ok(" 添加成功 ")
     */
    @Override
    public String insertNotice(NoticesBo bo) {
        this.checkNotice(bo);
        Notices notice = new Notices();
        notice.setIcon(bo.getIcon());
        notice.setContent(bo.getContent());
        notice.setTitle(bo.getTitle());
        notice.setNoticeId(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        notice.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

        noticeMapper.insertNotice(notice);

        this.createNoticeRedis();

        this.sendNoticeSocket(bo);

        return "添加成功";
    }

    private void sendNoticeSocket(Notices notices) {
        SendSocket socket = new SendSocket();
        //返回notify提示
        socket.setType("notify");
        socket.setMessage("");

        Notify notify = new Notify();
        notify.setDuration(0);
        notify.setTitle("系统公告：" + notices.getTitle());
        notify.setMessage(notices.getContent());
        notify.setColor("red");
        notify.setIcon(notices.getIcon());

        socket.setData(notify);

        //发送全体通知
        webSocket.sendAllMessage(socket);
    }

    @Override
    public R<PageVo> getPageVoSearch(NoticesBo noticesBo, PageBo pageBo) {
        List<Notices> list = getNoticeList();

        String noticeId = noticesBo.getNoticeId();
        if (StringUtils.isNotEmpty(noticeId) && StringUtils.isNotEmpty(list)) {
            list = list.stream().filter(o ->StringUtils.isNotEmpty(o.getNoticeId()) && o.getNoticeId().equals(noticeId)).collect(Collectors.toList());
        }

        String title = noticesBo.getTitle();
        if (StringUtils.isNotEmpty(title) && StringUtils.isNotEmpty(list)) {
            list = list.stream().filter(o ->StringUtils.isNotEmpty(o.getTitle()) && o.getTitle().equals(title)).collect(Collectors.toList());
        }

        String content = noticesBo.getContent();
        if (StringUtils.isNotEmpty(content) && StringUtils.isNotEmpty(list)) {
            list = list.stream().filter(o ->StringUtils.isNotEmpty(o.getContent()) && o.getContent().equals(content)).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(list)) {
            return R.ok(new PageVo(pageBo, list));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    @Override
    public String updateNotice(NoticesBo bo) {
        noticeMapper.updateById(bo);

        this.createNoticeRedis();

        this.sendNoticeSocket(bo);

        return "修改成功";
    }

    @Override
    public String deleteNotice(List<Integer> list) {
        LambdaQueryWrapper<Notices> lqw = new LambdaQueryWrapper<>();
        lqw.in(Notices::getId, list);
        noticeMapper.delete(lqw);

        this.createNoticeRedis();

        return "删除成功";
    }

    /**
     * 添加前检查公告
     */
    @SneakyThrows
    public void checkNotice(Notices notice) {
        if (StringUtils.isEmpty(notice.getTitle())) {
            throw new Exception("数据检查出现异常：公告名称不能为空");
        }
        if (StringUtils.isEmpty(notice.getContent())) {
            throw new Exception("数据检查出现异常：公告内容不能为空");
        }
        if (StringUtils.isEmpty(notice.getIcon())) {
            throw new Exception("数据检查出现异常：公告图标不能为空");
        }
    }

}

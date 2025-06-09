package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.LogBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Log;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.LogMapper;
import com.example.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    @Autowired
    private LogMapper logMapper;

    /**
     * 获取日志列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<Log> logs = this.getList();
        return new PageVo(pageBo, logs);

    }

    /**
     * 获取日志列表-不分页
     */
    public List<Log> getList() {
        LambdaQueryWrapper<Log> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Log::getId);
        return logMapper.selectList(lqw);
    }

    /**
     * 搜索日志列表-分页
     */
    @Override
    public R<PageVo> getPageVoSearch(LogBo bo, PageBo pageBo) {
        List<Log> list = this.getList();

        Integer id = bo.getId();
        if(StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getId()) && o.getId().equals(id)).collect(Collectors.toList());
        }

        String account = bo.getAccount();
        if(StringUtils.isNotEmpty(account) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getAccount()) && o.getAccount().contains(account)).collect(Collectors.toList());
        }

        String url = bo.getUrl();
        if(StringUtils.isNotEmpty(url) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getUrl()) && o.getUrl().contains(url)).collect(Collectors.toList());
        }

        String uri = bo.getUri();
        if(StringUtils.isNotEmpty(uri) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getUri()) && o.getUri().contains(uri)).collect(Collectors.toList());
        }

        String params = bo.getParams();
        if(StringUtils.isNotEmpty(params) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getParams()) && o.getParams().contains(params)).collect(Collectors.toList());
        }

        String mobile = bo.getMobile();
        if(StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getMobile()) && o.getMobile().contains(mobile)).collect(Collectors.toList());
        }

        String innerIp = bo.getInnerIp();
        if(StringUtils.isNotEmpty(innerIp) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getInnerIp()) && o.getInnerIp().contains(innerIp)).collect(Collectors.toList());
        }

        String methodApi = bo.getMethodApi();
        if(StringUtils.isNotEmpty(methodApi) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getMethodApi()) && o.getMethodApi().contains(methodApi)).collect(Collectors.toList());
        }

        String classApi = bo.getClassApi();
        if(StringUtils.isNotEmpty(classApi) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getClassApi()) && o.getClassApi().contains(classApi)).collect(Collectors.toList());
        }

        String ip = bo.getIp();
        if(StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(list)){
            list = list.stream().filter(o->StringUtils.isNotEmpty(o.getIp()) && o.getIp().contains(ip)).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(list)) {
            return R.ok(new PageVo(pageBo, list));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    /**
     * 新增日志
     */
    @Async
    @Override
    public void insertLog(Log log) {
        logMapper.insertLog(log);
    }

    @Override
    public String deleteLog(List<Integer> list) {
        LambdaQueryWrapper<Log> lqw = new LambdaQueryWrapper<>();
        lqw.in(Log::getId, list);
        logMapper.delete(lqw);
        return "删除成功";
    }
}

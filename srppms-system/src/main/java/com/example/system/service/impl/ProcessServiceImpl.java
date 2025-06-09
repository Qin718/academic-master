package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.ActivationConstant;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.ProcessBo;
import com.example.system.domain.entity.Process;
import com.example.system.domain.entity.User;
import com.example.system.domain.vo.UserVo;
import com.example.system.mapper.ProcessMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.ActivationService;
import com.example.system.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ActivationService activationService;

    /**
     * 获取某一项目的审核人员名单
     */
    @Override
    public List<Integer> getListShareUser(Integer projectId) {
        LambdaQueryWrapper<Process> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Process::getProjectId, projectId);
        List<Process> process = processMapper.selectList(lqw);
        List<Integer> userIds = process.stream().map(Process::getUserId).collect(Collectors.toList());
        return userMapper.selectList(new LambdaQueryWrapper<>()).stream().map(User::getId).filter(userIds::contains).collect(Collectors.toList());
    }

    /**
     * 获取搜索审核人员名单
     */
    @Override
    public List<Map<String, Object>> getListAllShareUser() {
        String key = RedisConstant.GET_LIST_SHARE_USER;
        List<UserVo> userVoList = redisUtils.get(key, UserVo.class);
        List<Map<String, Object>> list;
        list = userVoList.stream().map(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", o.getId());
            map.put("username", o.getUsername());
            return map;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 更改某一项目的审核人员名单
     */
    @Override
    public String changeUp(List<Integer> userIdS, Integer projectId) {
        LambdaQueryWrapper<Process> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Process::getProjectId, projectId);
        List<Process> processList = processMapper.selectList(lqw);
        List<Integer> userId = processList.stream().map(Process::getUserId).collect(Collectors.toList());
        List<Integer> delete = userId.stream().filter(o -> !userIdS.contains(o)).collect(Collectors.toList());
        List<Integer> insert = userIdS.stream().filter(o -> !userId.contains(o)).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delete)) {
            LambdaQueryWrapper<Process> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Process::getProjectId, projectId);
            for (Integer id : delete) {
                lqw.eq(Process::getUserId, id);
                processMapper.delete(lqw1);
            }
        }
        List<Process> insertList = new ArrayList<>();
        if (StringUtils.isNotEmpty(insert)) {
            for (Integer id : insert) {
                Process process = new Process();
                process.setUserId(0);
                process.setProjectId(projectId);
                process.setUserId(id);
                insertList.add(process);
            }
            processMapper.insertProcessList(insertList);
        }
        return "分配成功";
    }

    /**
     * 创建 审核人员名单 缓存
     */
    @Override
    public void createShareUserRedis() {
        List<UserVo> userList = userMapper.getUserListVo();
        userList = userList.stream().peek(o -> o.setImage("")).filter(o -> o.getAccess() != null && o.getAccess() <= 5).collect(Collectors.toList());
        long time = RedisConstant.GET_LIST_SHARE_USER_TIME;
        String key = RedisConstant.GET_LIST_SHARE_USER;
        redisUtils.set(key, userList, time);
    }

    /**
     * 审核项目
     */
    @Override
    public String updateProcess(ProcessBo processBo) {
        if ("NO".equals(processMapper.selectById(processBo.getId()).getIsProcess())) {
            activationService.updateActivation(processBo.getUserId(), ActivationConstant.PROCESS);
        }
        processBo.setIsProcess("YES");
        processMapper.updateById(processBo);
        return "提交成功";
    }


}

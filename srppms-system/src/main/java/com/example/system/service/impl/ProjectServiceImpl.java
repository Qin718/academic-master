package com.example.system.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.ActivationConstant;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.ProjectBo;
import com.example.system.domain.entity.Item;
import com.example.system.domain.entity.Project;
import com.example.system.domain.entity.Score;
import com.example.system.domain.model.ProjectKind;
import com.example.system.domain.model.ProjectKindInfo;
import com.example.system.domain.vo.ProjectVo;
import com.example.system.mapper.ItemMapper;
import com.example.system.mapper.ProjectMapper;
import com.example.system.mapper.ScoreMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.ActivationService;
import com.example.system.service.ItemService;
import com.example.system.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ActivationService activationService;

    /**
     * 获取当前年份
     */
    public static int getThisYear() {
        Calendar date = Calendar.getInstance();
        return date.get(Calendar.YEAR);
    }

    /**
     * 项目统计-获取今年科研项目的种类
     */
    @Override
    public List<ProjectKind> getProjectKindThisYear() {
        String year = String.valueOf(getThisYear());
        return getProjectKindList(year);
    }

    /**
     * 项目统计-获取去年科研项目的种类
     */
    @Override
    public List<ProjectKind> getProjectKindLastYear() {
        String year = String.valueOf(getThisYear() - 1);
        return getProjectKindList(year);
    }

    /**
     * 项目统计-今年
     */
    @Override
    public ProjectKindInfo getProjectKindInfoThisYear() {
        String year = String.valueOf(getThisYear());
        return getProjectKindInfo(year);
    }

    /**
     * 项目统计-去年
     */
    @Override
    public ProjectKindInfo getProjectKindInfoLastYear() {
        String year = String.valueOf(getThisYear() - 1);
        return getProjectKindInfo(year);
    }

    /**
     * 用户对某一项目进行评分
     */
    @Override
    public R<String> projectScore(ProjectBo bo) {
        String account = (String) StpUtil.getLoginId();
        int userId = userMapper.getUserByAccount(account).getId();
        LambdaQueryWrapper<Score> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Score::getProjectId, bo.getId());
        lqw.eq(Score::getUserId, userId);
        boolean exists = scoreMapper.exists(lqw);
        if (exists) {
            return R.warn("禁止用户重复打分！");
        }
        activationService.updateActivation(userId, ActivationConstant.SCORE);
        Score score = new Score();
        score.setProjectId(bo.getId());
        score.setUserId(userId);
        score.setScore(bo.getScore());
        scoreMapper.insertScore(score);
        itemService.createItemRedis();
        return R.ok("打分成功");
    }

    /**
     * 今年发表的项目数量列表
     */
    @Override
    public List<Integer> getNumber() {
        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<>();
        lqw.like(Item::getCreateTime, getThisYear());
        List<Item> items = itemMapper.selectList(lqw);
        Map<Integer, List<Item>> maps = items.stream().collect(Collectors.groupingBy(o -> Integer.parseInt(o.getCreateTime().toString().split(" ")[0].split("-")[1]) - 1));
        List<Integer> list = Arrays.stream(new Integer[12]).map(o -> 0).collect(Collectors.toList());
        maps.forEach((k, v) -> list.set(k, v.size()));
        return list;
    }

    /**
     * 查看某一年项目种类分布
     */
    public List<ProjectKind> getProjectKindList(String year) {
        List<ProjectKind> modelProjectKinds = projectMapper.getProjectKindList(year);
        if (StringUtils.isNotEmpty(modelProjectKinds)) {
        //给数据配置颜色属性
            renderingColor(modelProjectKinds);
        }
        return modelProjectKinds;
    }

    /**
     * 渲染颜色
     */
    private void renderingColor(List<ProjectKind> modelProjectKinds) {
        int len = modelProjectKinds.size();
        modelProjectKinds.get(0).setItemStyle(getColor());
        for (int i = 1; i < len; i++) {
            while (true) {
                String color = getColor();
                //确保每一个跟前一个颜色不相同
                if (!Objects.equals(color, modelProjectKinds.get(i - 1).getItemStyle())) {
                    modelProjectKinds.get(i).setItemStyle(color);
                    break;
                }
            }
        }
        //确保最后一个不跟第一个颜色相同
        if (len > 1) {
            while (Objects.equals(modelProjectKinds.get(len - 1).getItemStyle(), modelProjectKinds.get(0).getItemStyle())) {
                modelProjectKinds.get(len - 1).setItemStyle(getColor());
            }
        }
    }

    /**
     * 随机生成颜色
     */
    private String getColor() {
        String[] colorList = {
                "#6EB6F2", "#71F16F", "#FFC000", "#384C6C",
                "#7577F8", "#003366", "#AEABAF", "#00F4D2",
                "#8084F1", "#FF9F4E", "#4FA2A9", "#9a7fd1",
                "#588dd5", "#f5994e", "#c05050", "#59678c",
                "#c9ab00", "#7eb00a", "#6f5553", "#c14089",
                "#FF4F7F", "#CC6666", "#7f7522", "#2b4490",
                "#333399", "#70a19f", "#009299", "#78331e",
                "#3e4145", "#7bbfea", "#339999", "#8f4b38",
                "#694d9f", "#f26522", "#8e7437", "#45b97c",
                "#74787c", "#afdfe4", "#fdb933", "#bed742",
                "#A20055", "#AA0000", "#C63300", "#0000AA",
                "#2200AA", "#990099", "#00AAAA", "#00AA88",
                "#00AA55", "#FF1493"
        };
        int length = colorList.length;
        Random random = new Random();
        return colorList[random.nextInt(length)];
    }

    /**
     * 项目统计-某一年
     */
    private ProjectKindInfo getProjectKindInfo(String year) {
        List<ProjectVo> items = projectMapper.getProjectListByYear(year);
        //批量生成数据后，可以取消下面的判断
        if (items.isEmpty()) {
            return new ProjectKindInfo();
        }
        Double money = items.stream().map(ProjectVo::getMoney).reduce(Double::sum).get();
        int success = (int) items.stream().filter(o -> "审核通过".equals(o.getStatus())).count();
        //最受欢迎的项目
        ProjectKindInfo projectKindInfo = projectMapper.selectProjectByYear(year);
//        List<String> username = itemMapper.selectMaxProject(year);
        //最成功的作者（s）
//        projectKindInfo.setName(username);
        //花销
        projectKindInfo.setMoney(money);
        //成功的项目
        projectKindInfo.setSuccess(success);
        //新增项目数量
        projectKindInfo.setNewAdd(items.size());
        return projectKindInfo;
    }
}

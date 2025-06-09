package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.ConfigBo;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.entity.Config;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.ConfigMapper;
import com.example.system.service.ConfigService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    /**
     * 获取参数列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<Config> list = this.getList();
        return new PageVo(pageBo, list);
    }

    /**
     * 获取参数列表-不分页
     */
    private List<Config> getList() {
        return configMapper.getConfigList();
    }

    /**
     * 添加参数
     */
    @Override
    public String insertConfig(Config config) {
        this.checkConfigBeforeInsert(config);
        configMapper.insertConfig(config);
        return "添加成功";
    }

    /**
     * 删除参数
     */
    @Override
    public String deleteConfig(List<Integer> list) {
        LambdaQueryWrapper<Config> lqw = new LambdaQueryWrapper<>();
        lqw.in(Config::getId, list);
        configMapper.delete(lqw);
        return "删除成功";
    }

    /**
     * 修改参数
     */
    @Override
    public String updateConfig(Config config) {
        this.checkConfigBeforeUpdate(config);
        configMapper.updateById(config);
        return "修改成功";
    }

    /**
     * 获取参数列表-分组
     */
    @Override
    public Map<String, List<Config>> getMap() {
        List<Config> list = this.getList();
        return list.stream().collect(Collectors.groupingBy(Config::getConfig));
    }

    @Override
    public R<PageVo> getPageVoBySearch(ConfigBo configBo, PageBo pageBo) {
        List<Config> list = this.getList();
        Integer id = configBo.getId();
        if (StringUtils.isNotEmpty(id)) {
            list = list.stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList());
        }
        String key = configBo.getConfig();
        if (StringUtils.isNotEmpty(key)) {
            list = list.stream().filter(o -> o.getConfig().contains(key)).collect(Collectors.toList());
        }
        String value = configBo.getValue();
        if (StringUtils.isNotEmpty(value)) {
            list = list.stream().filter(o -> o.getValue().contains(value)).collect(Collectors.toList());
        }

        String remark = configBo.getRemark();
        if (StringUtils.isNotEmpty(remark)) {
            list = list.stream().filter(o -> o.getRemark().contains(remark)).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(list)) {
            return R.ok(new PageVo(pageBo, list));
        }
        return R.info("没有符合条件的数据", new PageVo());

    }

    /**
     * 修改前校验
     */
    @SneakyThrows
    private void checkConfigBeforeUpdate(Config config) {
        checkConfig(config);
        LambdaQueryWrapper<Config> lqw = new LambdaQueryWrapper<>();
        lqw.ne(Config::getId, config.getId());
        lqw.eq(Config::getConfig, config.getConfig());
        lqw.eq(Config::getValue, config.getValue());
        boolean configExists = configMapper.exists(lqw);
        if (configExists) {
            throw new Exception("修改失败：参数重复！");
        }
    }

    /**
     * 公共校验
     */
    @SneakyThrows
    private void checkConfig(Config config) {
        if (StringUtils.isEmpty(config.getConfig())) {
            throw new Exception("数据检查出现异常：参数标识符不能为空");
        }
        if (StringUtils.isEmpty(config.getValue())) {
            throw new Exception("数据检查出现异常：参数内容不能为空");
        }
    }

    /**
     * 添加前校验
     */
    @SneakyThrows
    private void checkConfigBeforeInsert(Config config) {
        checkConfig(config);
        List<Config> list = this.getList();
        list = list.stream().filter(o -> o.getConfig().equals(config.getConfig()) && o.getValue().equals(config.getValue())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(list)) {
            throw new Exception("添加失败：参数已存在！");
        }
    }

}

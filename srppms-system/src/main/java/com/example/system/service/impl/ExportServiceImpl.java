package com.example.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.expect.expectToExcel;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.system.domain.entity.User;
import com.example.system.mapper.UserMapper;
import com.example.system.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExportServiceImpl implements ExportService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 导出用户列表
     */
    @Override
    public R<Map<String, Object>> exportEmployees(List<Integer> ids) {
        //获取导出的用户列表
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<>());
        if (!ids.isEmpty()) {
            users = users.stream().filter(o -> ids.contains(o.getId())).collect(Collectors.toList());
        }
        boolean isTemplate = StringUtils.isEmpty(users);
        Map<String, Object> map = getExpectMap(User.class,isTemplate);
        map.put("fileName", "用户信息表");
        map.put("list", users);
        return R.ok("导出成功", map);
    }

    /**
     * 获取exl文件的表头和字段
     */
    public Map<String, Object> getExpectMap(Class<?> c, boolean isTemplate) {
        Field[] fields = c.getDeclaredFields();
        List<String> headers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (Field field : fields) {
            expectToExcel expectToExcel = field.getAnnotation(expectToExcel.class);
            if (expectToExcel != null) {
                if(isTemplate && !expectToExcel.template()){
                    continue;
                }
                String header = expectToExcel.header();
                headers.add(header);
                String name = field.getName();
                names.add(name);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("headers", headers);
        map.put("names", names);
        return map;
    }
}

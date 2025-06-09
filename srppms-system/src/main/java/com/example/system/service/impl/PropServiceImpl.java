package com.example.system.service.impl;


import com.example.common.prop.Prop;
import com.example.common.utils.StringUtils;
import com.example.system.domain.entity.*;
import com.example.system.domain.model.Interface;
import com.example.system.service.PropService;
import io.swagger.annotations.ApiModelProperty;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PropServiceImpl implements PropService {

    @Override
    public List<Map<String, Object>> getProp(String table) {
        if (table == null || table.isEmpty()) {
            return null;
        }
        //大写转小写
        table = table.toLowerCase();
        switch (table) {
            case "user":
                return getDomain(User.class);
            case "menu":
                return getDomain(Menu.class);
            case "role":
                return getDomain(Role.class);
            case "item":
                List<Map<String, Object>> list = getDomain(Item.class);
                list.addAll(getDomain(Project.class));
                return list;
            case "notice":
                return getDomain(Notices.class);
            case "log":
                return getDomain(Log.class);
            case "config":
                return getDomain(Config.class);
            case "interface":
                return getDomain(Interface.class);
        }
        return null;
    }

    @SneakyThrows
    public List<Map<String, Object>> getDomain(Class<?> clazz) {
        List<Map<String, Object>> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Prop p = field.getDeclaredAnnotation(Prop.class);
            ApiModelProperty annotation = field.getDeclaredAnnotation(ApiModelProperty.class);
            if (annotation == null && p == null) {
                continue;
            }
            if (p != null && !p.isProp()) {
                continue;
            }

            boolean checked = true;
            String label = "";
            String prop = field.getName();
            int index = i + 1;

            if (annotation != null) {
                label = annotation.value();
            }
            if (p != null) {
                if (StringUtils.isNotEmpty(p.label())) {
                    label = p.label();
                }
                if (p.index() != -1) {
                    index = p.index();
                }
                if (StringUtils.isNotEmpty(p.prop())) {
                    prop = p.prop();
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("label", label);
            map.put("prop", prop);
            map.put("checked", checked);
            map.put("index", index);
            list.add(map);
        }
        return list;
    }
}

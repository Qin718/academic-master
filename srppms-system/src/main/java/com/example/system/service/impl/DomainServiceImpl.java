package com.example.system.service.impl;


import com.baomidou.mybatisplus.annotation.TableName;
import com.example.common.constant.SystemConstant;
import com.example.common.utils.ClassUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.model.Domain;
import com.example.system.domain.vo.PageVo;
import com.example.system.service.DomainService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DomainServiceImpl implements DomainService {

    /**
     * 获取实体-分页
     */
    @Override
    @SneakyThrows
    public PageVo getPageVo(PageBo pageBo) {
        String packagePath = SystemConstant.DOMAIN_PATH;
        // 获取包下所有的类
        List<Class<?>> classes = ClassUtils.getClzFromPkg(packagePath);
        // List<Class<?>> classes = getClasses(packagePath);
        // 遍历类列表，获取每个类的属性
        List<Domain> mapList = new ArrayList<>();
        for (Class<?> clazz : classes) {
            Domain domain = new Domain();
            //类名称
            String[] names = clazz.getName().split("\\.");
            domain.setName(names[names.length - 1]);
            //类Api
            Api api = clazz.getAnnotation(Api.class);
            if(StringUtils.isNotEmpty(api)){
                String apiValue = api.value();
                domain.setApiValue(apiValue);
            }
            //类数据表
            TableName tableName = clazz.getAnnotation(TableName.class);
            if(StringUtils.isNotEmpty(tableName)){
                String tableValue = tableName.value();
                domain.setTableValue(tableValue);
            }
            //类字段
            //自身的字段
            List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
            //集成的字段
            fields.addAll(Arrays.stream(clazz.getSuperclass().getDeclaredFields()).collect(Collectors.toList()));
            List<com.example.system.domain.model.Field> fieldList = fields.stream().map(o-> {
                com.example.system.domain.model.Field fie =new com.example.system.domain.model.Field();
                //名称
                fie.setName(o.getName());
                //类型
                String[] name = o.getType().getName().split("\\.");
                fie.setType(name[name.length - 1]);
                //说明
                ApiModelProperty apiModelProperty = o.getDeclaredAnnotation(ApiModelProperty.class);
                if(StringUtils.isNotEmpty(apiModelProperty)){
                    fie.setRemark(apiModelProperty.value());
                }
                return fie;
            }).collect(Collectors.toList());
            domain.setField(fieldList);
            mapList.add(domain);
        }
        //排序
        mapList.sort(Comparator.comparing(Domain::getApiValue));
        return new PageVo(pageBo, mapList);
    }

    /**
     * 获取包下所有的类
     */
    private List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * 在指定的目录下查找类文件
     */
    private List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                    classes.add(clazz);
                }
            }
        }
        return classes;
    }
}

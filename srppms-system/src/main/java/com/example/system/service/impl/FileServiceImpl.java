package com.example.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.response.R;
import com.example.system.domain.entity.File;
import com.example.system.mapper.FileMapper;
import com.example.system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Autowired
    private FileMapper fileMapper;

    /**
     * 获取项目附件
     */
    @Override
    public List<File> getFileList(Integer id) {
        LambdaQueryWrapper<File> lqw = new LambdaQueryWrapper<>();
        lqw.eq(File::getProjectId,id);
        List<File> list = fileMapper.selectList(lqw);
        list = list.stream().map(m -> {
            File file = new File();
            file.setId(m.getId());
            file.setName(m.getName());
            return file;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 下载项目附件
     */
    @Override
    public R<File> downloadFile(Integer id) {
        File file = fileMapper.selectById(id);
        return R.ok("正在下载", file);
    }
}

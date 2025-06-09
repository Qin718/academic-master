package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<File> {
    void insertFileList(@Param("list") List<File> fileList);
}

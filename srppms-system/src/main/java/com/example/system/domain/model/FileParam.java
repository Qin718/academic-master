package com.example.system.domain.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Api(value = "文件集合实体", tags = {"文件集合实体"})
public class FileParam {
    @ApiModelProperty("文件集合")
    @JSONField(serialize = false)
    private MultipartFile[] files;
}


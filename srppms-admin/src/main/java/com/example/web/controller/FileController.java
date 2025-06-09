package com.example.web.controller;

import com.example.common.response.R;
import com.example.system.domain.entity.File;
import com.example.system.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "文件管理", tags = {"文件管理"})
@RequestMapping(value = "/file", method = {RequestMethod.POST, RequestMethod.GET})
public class FileController {

    @Autowired
    private FileService fileService;

    @ResponseBody
    @ApiOperation("获取项目附件")
    @RequestMapping("/getFileList/{id}")
    public R<List<File>> getFileList(@PathVariable("id") Integer id) {
        return R.ok(fileService.getFileList(id));
    }

    @ResponseBody
    @ApiOperation("下载项目附件")
    @RequestMapping("/download/{id}")
    public R<File> downloadFile(@PathVariable("id") Integer id) {
        return fileService.downloadFile(id);
    }

}

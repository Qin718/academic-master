package com.example.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.FileUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.entity.Image;
import com.example.system.domain.entity.User;
import com.example.system.domain.model.FileParam;
import com.example.system.mapper.ImageMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上传操作
 **/
@RestController
@Api(value = "上传管理", tags = {"上传管理"})
@RequestMapping(value = "/import", method = {RequestMethod.POST, RequestMethod.GET})
public class ImportController {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ImageMapper imageMapper;

    /* 图片 */
    @ResponseBody
    @SneakyThrows
    @ApiOperation("更新用户头像")
    @RequestMapping(value = "/change/image", method = RequestMethod.POST, consumes = "multipart/form-data")
    public R<String> image(FileParam files) {
        MultipartFile file = files.getFiles()[0];
        if (file.isEmpty()) {
            return R.fail("文件为空");
        }
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = file.getBytes();
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
        String base64 = Base64.getEncoder().encodeToString(fileBytes);

        if(StringUtils.isEmpty(base64)){
            throw new Exception("更新用户头像发生未知错误");
        }

        int image_id;
        List<Image> imageList = imageMapper.selectList(new LambdaQueryWrapper<>());
        Image image = new Image();
        boolean Add = false;
        if (StringUtils.isNotEmpty(imageList)) {
            imageList = imageList.stream().filter(o -> base64.equals(o.getImage())).collect(Collectors.toList());
            if (imageList.size() == 1) {
                image = imageList.get(0);
            } else {
                if (StringUtils.isEmpty(imageList)) {
                    Add = true;
                } else {
                    throw new Exception("更新用户头像发生未知错误");
                }
            }
        } else {
            Add = true;
        }
        if (Add) {
            //数据库中不存在此图片，向表格中添加照片数据流
            image = new Image();
            image.setImage(base64);
            imageMapper.insertImage(image);
        }
        image_id = image.getId();

        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        String account = (String) StpUtil.getLoginId();
        lqw.eq(User::getAccount, account);
        lqw.last("limit 1");
        User user = userMapper.selectOne(lqw);
        user.setImageId(image_id);
        userMapper.updateById(user);
        return R.ok("修改成功");
    }

    /* 附件 */
    @ResponseBody
    @ApiOperation("上传附件")
    @RequestMapping(value = "/file", method = RequestMethod.POST, consumes = "multipart/form-data")
    public R<String> file(FileParam files) {
        List<String> stringList = new ArrayList<>();
        fileUtils.uploadFileUtil(files.getFiles(), stringList);
        String account = (String) StpUtil.getLoginId();
        String k = RedisConstant.PROJECT_FILES + account;
        long time = RedisConstant.PROJECT_FILES_T_IME;
        redisUtils.set(k, stringList, time);
        return R.ok(null);
    }

    @ResponseBody
    @ApiOperation("导入用户")
    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "multipart/form-data")
    public R<String> importUser(FileParam files) {
        List<String> paths = new ArrayList<>();
        fileUtils.uploadFileUtil(files.getFiles(), paths);
        if (StringUtils.isEmpty(paths)) {
            return R.fail("文件为空");
        }
        return userService.importUser(paths);
    }
}

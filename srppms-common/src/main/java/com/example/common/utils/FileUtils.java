package com.example.common.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.example.common.constant.SystemConstant;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FileUtils {

    /**
     * 文件上传工具类
     */
    public void uploadFileUtil(MultipartFile[] files, List<String> stringList) {
        for (MultipartFile file : files) {
            String[] dayTime = LocalDateTimeUtil.formatNormal(LocalDateTime.now()).replace(":", "-").split(" ");
            String day = dayTime[0];
            String time = dayTime[1];
            String name = file.getOriginalFilename();
            //获取文件名
            String path = SystemConstant.FILE_UPLOAD + day + "\\" + time + "\\";
            // 检测是否存在该目录
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            path = path + name;
            try {
                FileOutputStream fos = new FileOutputStream(path);
                // 写入文件
                fos.write(file.getBytes());
                stringList.add(path);
                fos.close();
            } catch (Exception e) {
//            e.printStackTrace();
                System.out.println("Exception:" + e);
            }
        }
    }

    /**
     * 图片上传工具类
     */
    @SneakyThrows
    public String uploadImageUtil(MultipartFile file) {
        String[] dayTime = LocalDateTimeUtil.formatNormal(LocalDateTime.now()).replace(":", "-").split(" ");
        String day = dayTime[0];
        String time = dayTime[1];
        //获取文件名
        String fileName = "image.png";
        String path = SystemConstant.IMAGE_UPLOAD + day + "\\" + time + "\\";
        // 检测是否存在该目录
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        fileName = path + fileName;
        try {
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                // 写入文件
                fos.write(file.getBytes());
            }
            return fileName;
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Exception:" + e);
        }
        throw new Exception("上传失败");
    }

}

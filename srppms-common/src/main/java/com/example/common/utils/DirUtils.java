package com.example.common.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * 文件夹工具类
 */

public class DirUtils {

    /**
     * 删除文件夹以及所有文件
     */
    public static void deleteDir(String path) {
        Path directory = Paths.get(path);
        try (Stream<Path> stream = Files.walk(directory).sorted(Comparator.reverseOrder())) {
            System.out.println("===============删除本地文件===============");
            stream.forEach(o -> {
                try {
                    System.out.println("删除文件-> " + o);
                    Files.delete(o);
                } catch (IOException ignored) {
                }
            });
        } catch (IOException ignored) {
        }
    }
}


package com.example.system.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.utils.StringUtils;
import com.example.system.domain.entity.Config;
import com.example.system.domain.entity.Update;
import com.example.system.domain.vo.UpdateVo;
import com.example.system.mapper.ConfigMapper;
import com.example.system.mapper.UpdateMapper;
import com.example.system.service.UpdateService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UpdateServiceImpl extends ServiceImpl<UpdateMapper, Update> implements UpdateService {

    @Autowired
    private UpdateMapper updateMapper;
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<UpdateVo> getUpdateList() {
        List<UpdateVo> list = updateMapper.getList();
        list.forEach(updateVo -> {
            String content = updateVo.getContent();
            String time = updateVo.getTime();
            List<String> contents = Arrays.stream(content.split(time)).sorted().collect(Collectors.toList());
            updateVo.setContents(contents);
            updateVo.setContent("");
        });
        list = list.stream().sorted(Comparator.comparing(UpdateVo::getTime).reversed()).collect(Collectors.toList());
        return list;
    }

    /**
     * 爬取项目地址，更新项目日志
     */
    @Override
    public void createUpdateRedis() {
        //仓库所属空间地址(企业、组织或个人的地址path)
        String owner = "cai-bin00";
        //仓库路径(path)
        String repo = "srppms";
        //请求地址
        String url = "https://gitee.com/api/v5/repos/" + owner + "/" + repo + "/commits";
        //当前的页码
        int page = 1;
        //每页的数量，最大为 100
        int perPage = 100;
        //最后一次的日志
        LambdaQueryWrapper<Update> lqwUpdate = new LambdaQueryWrapper<>();
        lqwUpdate.orderByDesc(Update::getTime);
        lqwUpdate.last("limit 1");
        Update lastUpdate = updateMapper.selectOne(lqwUpdate);
        Integer id = 0;
        String time;
        if (StringUtils.isNotEmpty(lastUpdate)) {
            id = lastUpdate.getId();
            time = lastUpdate.getTime();
            String lastVersion = lastUpdate.getVersion();
            redisUtils.set(RedisConstant.VERSION, lastVersion);
        } else {
            time = "";
        }

        //开关
        LambdaQueryWrapper<Config> lqwConfig = new LambdaQueryWrapper<>();
        lqwConfig.eq(Config::getConfig, "update");
        Config config = configMapper.selectOne(lqwConfig);
        if (StringUtils.isEmpty(config) || "false".equals(config.getValue())) {
            return;
        }

        List<Update> updateList = new ArrayList<>();
        boolean T = true;
        while(T){
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url + "?" + "page=" + page + "&" + "per_page=" + perPage);
            page = page + 1;
            httpGet.setHeader("use-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
            CloseableHttpResponse response;
            try {
                response = httpClient.execute(httpGet);
                int code = response.getStatusLine().getStatusCode();
                if (code == 200) {
                    String html = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JSONArray commits = JSONUtil.parseArray(html);
                    if (StringUtils.isNotEmpty(commits)) {
                        List<Update> updates = commits.stream()
                        .map(o -> ((JSONObject) o).get("commit")).filter(o -> {
                            if ("".equals(time)) {
                                return true;
                            }
                            String date = ((JSONObject) ((JSONObject) o).get("author")).get("date").toString().replace("T", " ").split("\\+")[0];
                            return date.compareTo(time) >= 0;
                        }).map(o -> {
                            JSONObject object = (JSONObject) o;
                            Update update = new Update();

                            String date = ((JSONObject) object.get("author")).get("date").toString().split("T")[0];
                            update.setTime(date);

                            JSONObject author = (JSONObject) object.get("author");
                            String name = (String) author.get("name");
                            update.setName(name);

                            String content = (String) object.get("message");
                            content = content.replace("。", "。\n") + "\n";
                            if (content.startsWith("1.")) {
                                List<String> list = new ArrayList<>();
                                String[] split = content.split("\n");
                                Arrays.stream(split).forEach(s -> {
                                    if (StringUtils.isNotEmpty(s)) {
                                        String[] split1 = s.split("\\.");
                                        if (split1.length > 1) {
                                            list.add(split1[1]);
                                        } else {
                                            list.add(s);
                                        }
                                    }
                                });
                                List<String> list1 = list.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
                                content = String.join(date, list1);
                            }

                            if (content.contains("\n")) {
                                content = String.join(" ", content.split("\n"));
                            }
                            update.setContent(content);
                            return update;
                        }).collect(Collectors.toList());

                        updateList.addAll(updates);
                    }else {
                        T = false;
                    }
                }else {
                    T = false;
                }
                httpClient.close();
                response.close();
            } catch (IOException e) {
                System.out.println("****************************");
                System.out.println("*****获取项目更新日志失败!*****");
                System.out.println("****************************");
            }
        }

        if(StringUtils.isNotEmpty(updateList)){

            updateMapper.deleteById(id);

            Map<String, List<Update>> map = updateList.stream().collect(Collectors.groupingBy(Update::getTime));

            ArrayList<Map.Entry<String, List<Update>>> list = new ArrayList<>(map.entrySet());
            list.sort(Map.Entry.comparingByKey());

            id = Math.max(id, 1);
            for (Map.Entry<String, List<Update>> stringListEntry : list) {
                String date = stringListEntry.getKey();
                List<Update> updates = stringListEntry.getValue();

                Update update = new Update();
                update.setId(id);
                update.setTime(date);

                int v = 99 + id;
                String version = "";
                version = "." + v % 10 + version;
                v = v / 10;
                version = "." + v % 10 + version;
                v = v / 10;
                version = "v" + v % 10 + version;
                update.setVersion(version);

                //                version= "v" + String.join(".", v.split(""));
                String title = "项目更新" + " - " + version + " - " + date;
                update.setTitle(title);

                //name
                String name = updates.stream().map(Update::getName).distinct().collect(Collectors.joining(" "));
                update.setName(name);

                //content
                List<String> contents = updates.stream().map(Update::getContent).distinct().collect(Collectors.toList());
                String content = String.join(date, contents).replace("\n", "");
                update.setContent(content);
                id++;
                updateMapper.insertUpdateId(update);
            }
        }
    }
}

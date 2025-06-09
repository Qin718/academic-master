package com.example.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.system.domain.entity.Item;
import com.example.system.domain.entity.Project;
import com.example.system.mapper.ProjectMapper;
import com.example.system.mapper.UserMapper;
import com.example.system.service.CountService;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CountServiceImpl implements CountService {
    final String key = RedisConstant.GET_LIST_ITEM;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 各种类科研项目的发表-折线图
     */
    @Override
    public Map<String, Object> getProjectLine() {
        List<Project> projects = projectMapper.selectList(new LambdaQueryWrapper<>());
        List<Item> items = redisUtils.get(key, Item.class);
        List<Item> list = items.stream().sorted(Comparator.comparing(Item::getCreateTime)).collect(Collectors.toList());
        //第一个
        Item first = list.get(0);
        //最后一个
        Item last = list.get(list.size() - 1);
        int endYear = Integer.parseInt(last.getCreateTime().toString().split("-")[0]);
        int endMouth = Integer.parseInt(last.getCreateTime().toString().split("-")[1]);
        List<String> types = projects.stream().map(Project::getType).distinct().collect(Collectors.toList());
        Map<String, List<Item>> listGroupByType = items.stream().collect(Collectors.groupingBy(item -> item.getProject().getType()));
        List<List<Long>> data = new ArrayList<>();
        //横坐标
        List<String> x = new ArrayList<>();
        List<String> finalX = x;
//        list_groupBy_type.values().stream().peek(itemList -> {
//            int startYear = Integer.parseInt(first.getCreateTime().toString().split("-")[0]);
//            int startMouth = Integer.parseInt(first.getCreateTime().toString().split("-")[1]);
//            List<Long> list1 = new ArrayList<>();
//            while (startYear < end_year || (startYear == end_year && startMouth <= end_mouth)) {
//                String x = startYear + "-" + startMouth;
//                finalX_.add(x);
//                long count = itemList.stream().filter(o -> o.getCreateTime().toString().startsWith(x)).count();
//                list1.add(count);
//                startMouth++;
//                if (startMouth == 13) {
//                    if (startYear == end_year) break;
//                    startMouth = 1;
//                    startYear++;
//                }
//            }
//            data.add(list1);
//        }).collect(Collectors.toList());
        listGroupByType.forEach((k, v) -> {
            int startYear = Integer.parseInt(first.getCreateTime().toString().split("-")[0]);
            int startMouth = Integer.parseInt(first.getCreateTime().toString().split("-")[1]);
            List<Long> list1 = new ArrayList<>();
            while (startYear < endYear || (startYear == endYear && startMouth <= endMouth)) {
                String x1 = startYear + "-" + startMouth;
                finalX.add(x1);
                long count = v.stream().filter(o -> o.getCreateTime().toString().startsWith(x1)).count();
                list1.add(count);
                startMouth++;
                if (startMouth == 13) {
                    if (startYear == endYear) {
                        break;
                    }
                    startMouth = 1;
                    startYear++;
                }
            }
            data.add(list1);
        });//2265
        Map<String, Object> map = new HashMap<>();
        //横坐标
        x = finalX.stream().distinct().collect(Collectors.toList());
        map.put("x", x);
        //折线数据
        map.put("data", data);
        //项目种类
        map.put("type", types);
        return map;
    }

    /**
     * 用户注册趋势图-横向柱状图
     */
    @Override
    public List<Map<String, Integer>> getUserInsert() {
        return userMapper.getUserInsertCount();
    }

    /**
     * 各种类科研项目的发表-动态排序折线图
     */
    @Override
    public Map<String, Object> getDynamicSortLineChart() {
        List<Item> items = redisUtils.get(key, Item.class);
        List<Item> list = items.stream().sorted(Comparator.comparing(Item::getCreateTime)).collect(Collectors.toList());
        //第一个
        int first = Integer.parseInt(list.get(0).getCreateTime().toString().split("-")[0]);
        //最后一个
        int last = Integer.parseInt(list.get(list.size() - 1).getCreateTime().toString().split("-")[0]);
        List<String> types = items.stream().map(Item::getProject).collect(Collectors.toList()).stream().map(Project::getType).distinct().collect(Collectors.toList());
        Map<String, List<Item>> listGroupByType = items.stream().collect(Collectors.groupingBy(item -> item.getProject().getType()));
        List<List<Object>> listList = new ArrayList<>();
        List<Object> l = new ArrayList<>();
        l.add("type");
        l.add("year");
        l.add("num");
        listList.add(l);
        listGroupByType.forEach((type, item) -> {
            for (int i = first; i < last; i++) {
                List<Object> l1 = new ArrayList<>();
                l1.add(type);
                int y = i;
                l1.add(y);
                long num = item.stream().filter(o -> Integer.parseInt(o.getCreateTime().toString().split("-")[0]) <= y).count();
                l1.add(num);
                listList.add(l1);
            }
        });
        Map<String, Object> map = new HashMap<>();
        //数据
        map.put("data", listList);
        //最小年份
        map.put("year", first);
        //项目种类
        map.put("type", types);
        return map;
    }

    /**
     * 项目贡献度-表格
     */
    @Override
    @SneakyThrows
    public Map<String, Object> getContribution() {
        String url = "https://gitee.com/cai-bin00_admin";
        Document document = Jsoup.parse(new URL(url), 1000000);

        Elements elements = document.getElementsByClass("right-side");
        String rightSide = elements.toString();

        String regex = "data-content=\"(.*?)个贡献：(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rightSide);
        List<List<Object>> data = new ArrayList<>();

        while (matcher.find()) {
            System.out.println(matcher.group(1) + " " + matcher.group(2));
            List<Object> list = new ArrayList<>();
            list.add(matcher.group(2));
            list.add(Integer.parseInt(matcher.group(1)));
            data.add(list);
        }

        Map<String, Object> resultMap = new HashMap<>();

        String start = (String) data.get(0).get(0);
        String end = (String) data.get(data.size() - 1).get(0);
        List<String> date = Arrays.asList(start, end);
        resultMap.put("date", date);
        resultMap.put("data", data);
        return resultMap;
    }

}

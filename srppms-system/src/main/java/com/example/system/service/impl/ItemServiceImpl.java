package com.example.system.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.ActivationConstant;
import com.example.common.constant.RedisConstant;
import com.example.common.redis.RedisUtils;
import com.example.common.response.R;
import com.example.common.utils.StringUtils;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.ProjectBo;
import com.example.system.domain.entity.Process;
import com.example.system.domain.entity.*;
import com.example.system.domain.vo.PageVo;
import com.example.system.mapper.*;
import com.example.system.service.ActivationService;
import com.example.system.service.ItemService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目日志(SysItem)表服务实现类
 */
@Service("ItemService")
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private ActivationService activationService;
    @Autowired
    private FileMapper fileMapper;

    /**
     * 获取项目列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<Item> list = this.getList();
        return new PageVo(pageBo, list);
    }

    /**
     * 获取我的项目列表-不分页
     */
    public List<Item> getMyItemList() {
        String account = (String) StpUtil.getLoginId();
        String key = RedisConstant.GET_LIST_ITEM_MY_LIST + account;
        if (redisUtils.hasKey(key)) {
            return redisUtils.get(key, Item.class);
        }
        List<Item> items = getList();
        items = items.stream().filter(o -> account.equals(o.getUser().getAccount())).collect(Collectors.toList());
        //缓存过期时间1天
        long time = RedisConstant.GET_LIST_ITEM_MY_LIST_TIME;
        redisUtils.set(key, items, time);
        return items;
    }

    /**
     * 创建项目缓存
     */
    @Override
    public void createItemRedis() {
        List<Item> itemList = selectAllItem();
        long time = RedisConstant.GET_LIST_ITEM_TIME;
        String key = RedisConstant.GET_LIST_ITEM;
        redisUtils.set(key, itemList, time);
    }

    /**
     * 搜索项目列表-分页
     */
    @Override
    public R<PageVo> getPageVoSearch(ProjectBo projectBo, PageBo pageBo) {
        List<Item> list = getList();

        Integer id = projectBo.getId();
        if(StringUtils.isNotEmpty(id)){
            list = list.stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList());
        }

        String name = projectBo.getName();
        if(StringUtils.isNotEmpty(name)){
            list = list.stream().filter(o -> StringUtils.isNotEmpty(o.getProject()) && StringUtils.isNotEmpty(o.getProject().getName()) && o.getProject().getName().contains(name)).collect(Collectors.toList());
        }

        String content = projectBo.getContent();
        if(StringUtils.isNotEmpty(content)){
            list = list.stream().filter(o -> StringUtils.isNotEmpty(o.getProject()) && StringUtils.isNotEmpty(o.getProject().getContent()) && o.getProject().getContent().contains(content)).collect(Collectors.toList());
        }

        String type = projectBo.getType();
        if(StringUtils.isNotEmpty(type)){
            list = list.stream().filter(o -> StringUtils.isNotEmpty(o.getProject()) && StringUtils.isNotEmpty(o.getProject().getType()) && o.getProject().getType().contains(type)).collect(Collectors.toList());
        }

        String createByName = projectBo.getCreateByName();
        if(StringUtils.isNotEmpty(createByName)){
            list = list.stream().filter(o -> StringUtils.isNotEmpty(o.getUser()) && StringUtils.isNotEmpty(o.getUser().getUsername()) && o.getUser().getUsername().contains(createByName)).collect(Collectors.toList());
        }

        String remark = projectBo.getRemark();
        if(StringUtils.isNotEmpty(remark)){
            list = list.stream().filter(o -> StringUtils.isNotEmpty(o.getProject()) && StringUtils.isNotEmpty(o.getProject().getRemark()) && o.getProject().getRemark().contains(remark)).collect(Collectors.toList());
        }

        String state = projectBo.getState();
        if(StringUtils.isNotEmpty(state)){
            list = list.stream().filter(o -> StringUtils.isNotEmpty(o.getStatus()) && o.getStatus().equals(state)).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(list)) {
            return R.ok(new PageVo(pageBo, list));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    /**
     * 添加项目
     */
    @Override
    public String insertItemBo(ProjectBo projectBo) {
        this.checkProject(projectBo);
        this.insertProject(projectBo);
        return "添加成功";
    }

    /**
     * 删除项目
     */
    @Override
    public String deleteItem(String projectId, String itemId) {
        projectMapper.deleteById(projectId);
        itemMapper.deleteById(itemId);
        this.updateItemsRedis();
        return "删除成功";
    }

    /**
     * 删除项目
     */
    @Override
    public String deleteItem(List<Integer> list) {
        list.forEach(id->{
            projectMapper.deleteById(id);
            itemMapper.deleteById(id);
        });
        this.updateItemsRedis();
        return "删除成功";
    }

    /**
     * 获取我的项目列表-分页
     */
    @Override
    public PageVo getPageVoMyItem(PageBo pageBo) {
        List<Item> list = this.getMyItemList();
        return new PageVo(pageBo, list);
    }

    /**
     * 获取项目列表-不分页
     */
    @Override
    public List<Item> getList() {
        return redisUtils.get(RedisConstant.GET_LIST_ITEM, Item.class);
    }

    /**
     * 搜索我的项目列表-不分页
     */
    public List<Item> getListMyItemSearch(String name) {
        List<Item> itemList = this.getMyItemList();
        itemList = itemList.stream().filter(o -> o.getProject().getName().contains(name)).collect(Collectors.toList());
        return itemList;
    }

    /**
     * 搜索我的项目列表-分页
     */
    @Override
    public R<PageVo> getPageVoMyItemSearch(ProjectBo projectBo, PageBo pageBo) {
        String account = (String) StpUtil.getLoginId();
        List<Item> itemList = redisUtils.get(RedisConstant.GET_LIST_ITEM_MY_LIST + account, Item.class);
        if (StringUtils.isNotEmpty(projectBo.getName())) {
            itemList = getListMyItemSearch(projectBo.getName());
        }
        if (StringUtils.isNotEmpty(itemList)) {
            return R.ok(new PageVo(pageBo, itemList));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    /**
     * 修改项目
     */
    @Override
    @SneakyThrows
    public String updateItem(ProjectBo projectBo) {
        this.checkProject(projectBo);
        //判断项目是否有附件
        String account = (String) StpUtil.getLoginId();
        String key = RedisConstant.PROJECT_FILES + account;
        if (redisUtils.hasKey(key)) {
            Integer projectId = projectBo.getId();
            StringBuilder files = new StringBuilder(projectBo.getFiles());
            List<String> list = redisUtils.get(key, String.class);
            for (String s : list) {
                String[] split = s.split("/");
                files.append(" ").append(split[split.length - 1]);
            }
            String s = "";
            for (String string : Arrays.stream(files.toString().split(" ")).filter(StringUtils::isNotEmpty).distinct().collect(Collectors.toList())) {
                if (StringUtils.isNotEmpty(s)) {
                    s = s + " ";
                }
                s = s + string;
            }
            projectBo.setFiles(s);
            List<File> fileList = new ArrayList<>();
            for (String path : list) {
                File file = new File();
                String[] split = path.replaceAll("\\\\", "/").split("/");
                String name = split[split.length - 1];
                file.setName(name.split("\\.")[0]);
                file.setType(name.split("\\.")[1]);
                file.setStream(encryptToBase64(path));
                file.setProjectId(projectId);
                fileList.add(file);
            }
            fileMapper.insertFileList(fileList);

        }
        LambdaQueryWrapper<Item> lqwItem = new LambdaQueryWrapper<>();
        lqwItem.eq(Item::getProjectId, projectBo.getId());
        Item item = itemMapper.selectOne(lqwItem);

        item.setUpdateTime(LocalDateTime.now());
        itemMapper.updateItem(item);
        projectMapper.updateById(projectBo);
        LambdaQueryWrapper<Process> lqwProcess = new LambdaQueryWrapper<>();
        lqwProcess.eq(Process::getProjectId, projectBo.getId());
        if (processMapper.exists(lqwProcess)) {
            List<Process> processList = processMapper.selectList(lqwProcess);
            processList = processList.stream().peek(o -> {
                o.setProcess("未审核");
                o.setRemark("因项目创建者修改项目，项目回到初始审核状态");
                o.setIsProcess("YES");
            }).collect(Collectors.toList());
            processMapper.updateList(processList);
        }
        this.updateItemsRedis();
        return "更新成功";
    }

    /**
     * 获取审核列表-分页
     */
    @Override
    public PageVo getPageVoProcess(PageBo pageBo) {
        List<Item> processList = this.getListProcess();
        return new PageVo(pageBo, processList);
    }

    /**
     * 搜索审核列表-不分页
     */
    public List<Item> getListProcessSearch(String name) {
        List<Item> itemList = this.getListProcess();
        itemList = itemList.stream().filter(o -> o.getProject().getName().contains(name)).collect(Collectors.toList());
        return itemList;
    }

    /**
     * 搜索审核列表-分页
     */
    @Override
    public R<PageVo> getPageVoProcessSearch(PageBo pageBo, ProjectBo projectBo) {
        List<Item> list = this.getListProcessSearch(projectBo.getName());
        if (StringUtils.isEmpty(list)) {
            return R.info("没有符合条件的数据", new PageVo());
        }
        return R.ok(new PageVo(pageBo, list));
    }

    /**
     * 更新项目缓存
     */
    @Async
    @Override
    public void updateItemsRedis() {
        Objects.requireNonNull(redisTemplate.keys(RedisConstant.GET_LIST_ITEM + "*")).forEach(o -> redisUtils.del(String.valueOf(o)));
        this.createItemRedis();
    }

    /**
     * 获取审核列表-不分页
     */
    public List<Item> getListProcess() {
        String account = (String) StpUtil.getLoginId();
        User user = userMapper.getUserByAccount(account);
        Integer userId = user.getId();
        // 根据当前用户Id，查到当前用户所有的审核项目
        LambdaQueryWrapper<Process> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Process::getUserId, userId);
        List<Process> processList = processMapper.selectList(lqw);
        // 提取项目的projectId，组成List
        List<Integer> projectIdLists = processList.stream().map(Process::getProjectId).collect(Collectors.toList());
        // 根据projectId列表找到Item列表
        String key = RedisConstant.GET_LIST_ITEM;
        List<Item> itemList = redisUtils.get(key, Item.class);
        if (!processList.isEmpty()) {
            itemList = itemList.stream().filter(o -> projectIdLists.contains(o.getProject().getId())).collect(Collectors.toList()).stream().peek(p -> p.setProcess(processList.stream().filter(o -> o.getProjectId().equals(p.getProject().getId())).collect(Collectors.toList()).get(0))).collect(Collectors.toList());
        }
        return itemList;
    }

    /**
     * 文件转为Base64
     *
     * @param filePath 文件的位置
     * @return 文件的Base64值
     */
    public String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.getEncoder().encodeToString(b);
        } catch (IOException e) {
            System.out.println("发生错误:" + e.getMessage());
        }
        return null;
    }

    /**
     * 检查项目
     */
    @SneakyThrows
    private void checkProject(Project project) {
        if (StringUtils.isEmpty(project.getName())) {
            throw new Exception("数据检查出现异常：项目名称不能为空");
        }
        if (StringUtils.isEmpty(project.getType())) {
            throw new Exception("数据检查出现异常：项目类型不能为空");
        }
        if (StringUtils.isEmpty(project.getContent())) {
            throw new Exception("数据检查出现异常：项目内容不能为空");
        }
    }

    /**
     * 搜索全部项目列表
     */
    private List<Item> selectAllItem() {
        List<Item> items = itemMapper.getItemList();
//        List<Integer> userIds = items.stream().map(Item::getCreateBy).collect(Collectors.toList());
//        List<User> users = userMapper.getUserByIds(userIds);
        List<User> users = userMapper.getUserList();
//        List<Integer> projectIds = items.stream().map(Item::getProjectId).collect(Collectors.toList());
//        List<Project> projects = projectMapper.getProjectByIds(projectIds);
        List<Project> projects = projectMapper.selectList(new LambdaQueryWrapper<>());
        items = items.stream().peek(i -> {
            if (StringUtils.isEmpty(i.getStatus())) {
                i.setStatus("未分配审核");
            }
            if (StringUtils.isEmpty(i.getScore())) {
                i.setScore(0.0);
            }
            User user = users.stream().filter(u -> u.getId().equals(i.getCreateBy())).collect(Collectors.toList()).stream().peek(o -> o.setImage("")).collect(Collectors.toList()).get(0);
            i.setUser(user);
//            List<User> users1 = users.stream().filter(u -> Objects.equals(u.getUserId(), i.getCreateBy())).collect(Collectors.toList()).stream().peek(o -> o.setImage("")).collect(Collectors.toList());
//            if (StringUtils.isNotEmpty(users1)) i.setUser(users1.get(0));
            Project project = projects.stream().filter(p -> Objects.equals(p.getId(), i.getProjectId())).collect(Collectors.toList()).get(0);
            i.setProject(project);
//            List<Project> projects1 = projects.stream().filter(p -> Objects.equals(p.getId(), i.getProjectId())).collect(Collectors.toList());
//            if (StringUtils.isNotEmpty(projects1)) i.setProject(projects1.get(0));
        }).collect(Collectors.toList());
        items = items.stream().sorted(Comparator.comparing(Item::getScore).reversed()).collect(Collectors.toList());
        return items;
    }

    /**
     * 添加新项目，并重新创建项目缓存
     */
    private void insertProject(Project project) {
        projectMapper.insertProject(project);
        User user = userMapper.getUserByAccount((String) StpUtil.getLoginId());
        Item item = new Item();
        item.setCreateBy(user.getId());
        item.setProjectId(project.getId());
        LocalDateTime time = LocalDateTime.now();
        item.setCreateTime(time);
        item.setUpdateTime(time);
        itemMapper.insertItem(item);
        //重新创建项目缓存
        this.updateItemsRedis();
        activationService.updateActivation(user.getId(), ActivationConstant.ADD_ITEM);
    }
}


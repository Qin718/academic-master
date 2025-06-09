package com.example.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.utils.StringUtils;
import com.example.system.domain.entity.Process;
import com.example.system.domain.entity.*;
import com.example.system.mapper.*;
import com.example.system.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private ActivationMapper activationMapper;
    @Autowired
    private DictMapper dictMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Value("${dataBase.check}")
    public boolean check;
    @Value("${dataBase.create.createFriendList}")
    public boolean createFriendList;
    @Value("${dataBase.create.createScoreList}")
    public boolean createScoreList;
    @Value("${dataBase.create.createProcessList}")
    public boolean createProcessList;

    /**
     * 检查数据库
     */
    @Override
    public void checkDataBase() {
        //  =========================检查数据库=========================
        if (check) {

//  =========================1.检查数据库是否存在数据=========================
            //dict
            // if (StringUtils.isEmpty(dictMapper.selectList(new LambdaQueryWrapper<>()))) {
            //     throw new RuntimeException("sys_dict表为空");
            // }
            //friend
            List<Friend> friendList = friendMapper.selectList(new LambdaQueryWrapper<>());
            boolean friendIsEmpty = StringUtils.isEmpty(friendList);
//            if (StringUtils.isEmpty(friendList)) {
//                throw new RuntimeException("sys_friend表为空");
//            }
            //image
            List<Image> imageList = imageMapper.selectList(new LambdaQueryWrapper<>());
            // if (StringUtils.isEmpty(imageList)) {
            //     throw new RuntimeException("sys_image表为空");
            // }
            //item
            List<Item> itemList = itemMapper.selectList(new LambdaQueryWrapper<>());
            boolean itemIsEmpty = StringUtils.isEmpty(itemList);
//            if (StringUtils.isEmpty(itemList)) {
//                throw new RuntimeException("sys_item表为空");
//            }
            //menu
            List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<>());
            // if (StringUtils.isEmpty(menuList)) {
            //     throw new RuntimeException("sys_menu表为空");
            // }
            //process
            List<Process> processList = processMapper.selectList(new LambdaQueryWrapper<>());
            boolean processIsEmpty = StringUtils.isEmpty(processList);
//            if (StringUtils.isEmpty(processList)) {
//                throw new RuntimeException("sys_process表为空");
//            }
            //project
            List<Project> projectList = projectMapper.selectList(new LambdaQueryWrapper<>());
            boolean projectIsEmpty = StringUtils.isEmpty(projectList);
//            if (StringUtils.isEmpty(projectList)) {
//                throw new RuntimeException("sys_project表为空");
//            }
            //role
            List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<>());
            // if (StringUtils.isEmpty(roleList)) {
            //     throw new RuntimeException("sys_role表为空");
            // }
            //score
            List<Score> scoreList = scoreMapper.selectList(new LambdaQueryWrapper<>());
            boolean scoreIsEmpty = StringUtils.isEmpty(scoreList);
//            if (StringUtils.isEmpty(scoreList)) {
//                throw new RuntimeException("sys_score表为空");
//            }
            //user
            List<User> userList = userMapper.selectList(new LambdaQueryWrapper<>());
            // if (StringUtils.isEmpty(userList)) {
            //     throw new RuntimeException("sys_user表为空");
            // }
            //user_role
            List<UserRole> userRoleList = userRoleMapper.selectList(new LambdaQueryWrapper<>());
            boolean userRoleIsEmpty = StringUtils.isEmpty(userRoleList);
//            if (StringUtils.isEmpty(userRoleList)) {
//                throw new RuntimeException("sys_user_role表为空");
//            }

//  =========================2.检查外键是否存在=========================
            //(1) 头像表
            // 约束 用户表
            List<Integer> imageListIds = imageList.stream().map(Image::getId).collect(Collectors.toList());
            List<Integer> imageIdNotExist = userList.stream().filter(o -> !imageListIds.contains(o.getImageId())).map(User::getId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(imageIdNotExist)) {
                System.out.println("用户表字段imageId未遵循外键约束，已删除错误数据");
                LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
                lqw.in(User::getImageId, imageIdNotExist);
                userMapper.delete(lqw);
            }
            //(2) 用户表
            // 约束 日志表
            List<String> logAccounts = logMapper.selectList(new LambdaQueryWrapper<>()).stream().map(Log::getAccount).distinct().collect(Collectors.toList());
            List<String> logAccountNotExist = logAccounts.stream().filter(o -> !userList.stream().map(User::getAccount).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(logAccountNotExist)) {
                System.out.println("日志表字段account未遵循外键约束，已删除错误数据");
                LambdaQueryWrapper<Log> lqw = new LambdaQueryWrapper<>();
                lqw.in(Log::getAccount, logAccountNotExist);
                logMapper.delete(lqw);
            }
            // 约束 登录记录表
            List<String> loginAccountS = loginMapper.selectList(new LambdaQueryWrapper<>()).stream().map(Login::getAccount).distinct().collect(Collectors.toList());
            List<String> loginAccountNotExist = loginAccountS.stream().filter(o -> !userList.stream().map(User::getAccount).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(loginAccountNotExist)) {
                System.out.println("登录记录表字段account未遵循外键约束，已删除错误数据");
                LambdaQueryWrapper<Login> lqw = new LambdaQueryWrapper<>();
                lqw.in(Login::getAccount, loginAccountNotExist);
                loginMapper.delete(lqw);
            }
            // 约束 活跃度表
            List<Activation> activationList = activationMapper.selectList(new LambdaQueryWrapper<>());
            List<Integer> activationUserId = activationList.stream().map(Activation::getUserId).distinct().collect(Collectors.toList());
            List<Integer> activationUserIdNotExist = activationUserId.stream().filter(o -> !userList.stream().map(User::getId).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(activationUserIdNotExist)) {
                System.out.println("活跃度表字段userId未遵循外键约束，已删除错误数据");
                LambdaQueryWrapper<Activation> lqw = new LambdaQueryWrapper<>();
                lqw.in(Activation::getUserId, activationUserIdNotExist);
                activationMapper.delete(lqw);
            }
            if(!friendIsEmpty){
                // 约束 好有表
                List<String> friendUserA = friendList.stream().map(Friend::getUserA).distinct().collect(Collectors.toList());
                List<String> friendUserB = friendList.stream().map(Friend::getUserB).distinct().collect(Collectors.toList());
                List<Integer> friendUserIdA = friendList.stream().map(Friend::getUserIdA).distinct().collect(Collectors.toList());
                List<Integer> friendUserIdB = friendList.stream().map(Friend::getUserIdB).distinct().collect(Collectors.toList());
                List<String> friendUserANotExist = friendUserA.stream().filter(o -> !userList.stream().map(User::getAccount).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
                List<String> friendUserBNotExist = friendUserB.stream().filter(o -> !userList.stream().map(User::getAccount).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
                List<Integer> friendUserIdANotExist = friendUserIdA.stream().filter(o -> !userList.stream().map(User::getId).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
                List<Integer> friendUserIdBNotExist = friendUserIdB.stream().filter(o -> !userList.stream().map(User::getId).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(friendUserANotExist) || StringUtils.isNotEmpty(friendUserBNotExist) ||
                        StringUtils.isNotEmpty(friendUserIdANotExist) || StringUtils.isNotEmpty(friendUserIdBNotExist)) {
                    LambdaQueryWrapper<Friend> lqw = new LambdaQueryWrapper<>();
                    if (StringUtils.isNotEmpty(friendUserANotExist)) {
                        lqw.or().in(Friend::getUserA, friendUserANotExist);
                        System.out.println("好友表字段userA未遵循外键约束，已删除错误数据");
                    }
                    if (StringUtils.isNotEmpty(friendUserBNotExist)) {
                        lqw.or().in(Friend::getUserB, friendUserBNotExist);
                        System.out.println("好友表字段userB未遵循外键约束，已删除错误数据");
                    }
                    if (StringUtils.isNotEmpty(friendUserIdANotExist)) {
                        lqw.or().in(Friend::getUserIdA, friendUserIdANotExist);
                        System.out.println("好友表字段userIdA未遵循外键约束，已删除错误数据");
                    }
                    if (StringUtils.isNotEmpty(friendUserIdBNotExist)) {
                        lqw.or().in(Friend::getUserIdB, friendUserIdBNotExist);
                        System.out.println("好友表字段userIdB未遵循外键约束，已删除错误数据");
                    }
                    friendMapper.delete(lqw);
                }
            }
            //(3) 多表约束同一张表
            // 菜单表和角色表 约束 角色菜单表
            List<RoleMenu> roleMenuList = roleMenuMapper.selectList(new LambdaQueryWrapper<>());
            if(StringUtils.isNotEmpty(roleMenuList)){
                List<Integer> menuId = menuList.stream().map(Menu::getId).collect(Collectors.toList());
                List<Integer> roleAccess = roleList.stream().map(Role::getAccess).collect(Collectors.toList());
                List<Integer> roleMenuMenuIdNotExist = roleMenuList.stream().map(RoleMenu::getMenuId).filter(id -> !menuId.contains(id)).collect(Collectors.toList());
                List<Integer> roleMenuAccessNotExist = roleMenuList.stream().map(RoleMenu::getAccess).filter(access -> !roleAccess.contains(access)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(roleMenuMenuIdNotExist) || StringUtils.isNotEmpty(roleMenuAccessNotExist)) {
                    LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
                    if (StringUtils.isNotEmpty(roleMenuMenuIdNotExist)) {
                        lqw.or().in(RoleMenu::getMenuId, roleMenuMenuIdNotExist);
                        System.out.println("角色菜单表字段menuId未遵循外键约束，已删除错误数据");
                    }
                    if (StringUtils.isNotEmpty(roleMenuAccessNotExist)) {
                        lqw.or().in(RoleMenu::getAccess, roleMenuAccessNotExist);
                        System.out.println("角色菜单表字段access未遵循外键约束，已删除错误数据");
                    }
                    roleMenuMapper.delete(lqw);
                }
            }
            List<Integer> userId = userList.stream().map(User::getId).collect(Collectors.toList());
            List<Integer> roleId = roleList.stream().map(Role::getId).collect(Collectors.toList());
            if(!userRoleIsEmpty){
                // 用户表和角色表 约束 用户角色表
                List<Integer> user_role_userId_Not_Exist = userRoleList.stream().map(UserRole::getUserId).filter(o -> !userId.contains(o)).collect(Collectors.toList());
                List<Integer> user_role_roleId_Not_Exist = userRoleList.stream().map(UserRole::getRoleId).filter(o -> !roleId.contains(o)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(user_role_userId_Not_Exist) || StringUtils.isNotEmpty(user_role_roleId_Not_Exist)) {
                    LambdaQueryWrapper<UserRole> lqw = new LambdaQueryWrapper<>();
                    if (StringUtils.isNotEmpty(user_role_userId_Not_Exist)) {
                        lqw.or().in(UserRole::getUserId, user_role_userId_Not_Exist);
                        System.out.println("用户角色表字段userId未遵循外键约束，已删除错误数据");
                    }
                    if (StringUtils.isNotEmpty(user_role_roleId_Not_Exist)) {
                        lqw.or().in(UserRole::getRoleId, user_role_roleId_Not_Exist);
                        System.out.println("用户角色表字段roleId未遵循外键约束，已删除错误数据");
                    }
                    userRoleMapper.delete(lqw);
                }
            }
            if(!itemIsEmpty) {
                List<Integer> projectId = projectList.stream().map(Project::getId).collect(Collectors.toList());
                if(!projectIsEmpty){
                    // 用户表和项目内容表 约束 项目表
                    List<Integer> item_userId_Not_Exist = itemList.stream().map(Item::getCreateBy).filter(o -> !userId.contains(o)).collect(Collectors.toList());
                    List<Integer> item_projectId_Not_Exist = itemList.stream().map(Item::getProjectId).filter(o -> !projectId.contains(o)).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(item_userId_Not_Exist) || StringUtils.isNotEmpty(item_projectId_Not_Exist)) {
                        LambdaQueryWrapper<Item> lqw = new LambdaQueryWrapper<>();
                        if (StringUtils.isNotEmpty(item_userId_Not_Exist)) {
                            lqw.or().in(Item::getCreateBy, item_userId_Not_Exist);
                            System.out.println("项目表字段createBy未遵循外键约束，已删除错误数据");
                        }
                        if (StringUtils.isNotEmpty(item_projectId_Not_Exist)) {
                            lqw.or().in(Item::getProjectId, item_projectId_Not_Exist);
                            System.out.println("项目表字段projectId未遵循外键约束，已删除错误数据");
                        }
                        itemMapper.delete(lqw);
                    }
                }
                if(!processIsEmpty) {
                    // 用户表和项目内容表 约束 审核表
                    List<Integer> process_userId_Not_Exist = processList.stream().map(Process::getUserId).filter(o -> !userId.contains(o)).collect(Collectors.toList());
                    List<Integer> process_projectId_Not_Exist = processList.stream().map(Process::getProjectId).filter(o -> !projectId.contains(o)).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(process_userId_Not_Exist) || StringUtils.isNotEmpty(process_projectId_Not_Exist)) {
                        LambdaQueryWrapper<Process> lqw = new LambdaQueryWrapper<>();
                        if (StringUtils.isNotEmpty(process_userId_Not_Exist)) {
                            lqw.or().in(Process::getUserId, process_userId_Not_Exist);
                            System.out.println("审核表字段userId未遵循外键约束，已删除错误数据");
                        }
                        if (StringUtils.isNotEmpty(process_projectId_Not_Exist)) {
                            lqw.or().in(Process::getProjectId, process_projectId_Not_Exist);
                            System.out.println("审核表字段projectId未遵循外键约束，已删除错误数据");
                        }
                        processMapper.delete(lqw);
                    }
                }
                if(!scoreIsEmpty){
                    // 用户表和项目内容表 约束 评分表
                    List<Integer> score_userId_Not_Exist = scoreList.stream().map(Score::getUserId).filter(o -> !userId.contains(o)).collect(Collectors.toList());
                    List<Integer> score_projectId_Not_Exist = scoreList.stream().map(Score::getProjectId).filter(o -> !projectId.contains(o)).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(score_userId_Not_Exist) || StringUtils.isNotEmpty(score_projectId_Not_Exist)) {
                        LambdaQueryWrapper<Score> lqw = new LambdaQueryWrapper<>();
                        if (StringUtils.isNotEmpty(score_userId_Not_Exist)) {
                            lqw.or().in(Score::getUserId, score_userId_Not_Exist);
                            System.out.println("评分表字段userId未遵循外键约束，已删除错误数据");
                        }
                        if (StringUtils.isNotEmpty(score_projectId_Not_Exist)) {
                            lqw.or().in(Score::getProjectId, score_projectId_Not_Exist);
                            System.out.println("评分表字段projectId未遵循外键约束，已删除错误数据");
                        }
                        scoreMapper.delete(lqw);
                    }
                }
            }

//  =========================3.检查一些表的数据条数是否一致=========================
            //(1) 用户表和用户角色表
            int userList_size = userList.size();
            int userRoleList_size = userRoleList.size();
            if (userRoleList_size != userList_size) {
                List<Integer> id = userList.stream().map(User::getId).filter(o -> !userRoleList.stream().map(UserRole::getUserId).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(id)) {
                    LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
                    lqw.in(User::getId, id);
                    userMapper.delete(lqw);
                }
                System.out.println("用户表和用户角色表数据条数不一致，已删除错误数据");
            }
            //(2) 项目表和项目内容表
            int itemList_size = itemList.size();
            int project_size = projectList.size();
            if (itemList_size != project_size) {
                List<Integer> id = projectList.stream().map(Project::getId).filter(o -> !itemList.stream().map(Item::getProjectId).collect(Collectors.toList()).contains(o)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(id)) {
                    LambdaQueryWrapper<Project> lqw = new LambdaQueryWrapper<>();
                    lqw.in(Project::getId, id);
                    projectMapper.delete(lqw);
                }
                System.out.println("项目表和项目内容表数据条数不一致，已删除错误数据");
            }

//  =========================4.检查一些数据库中不允许出现的数据=========================
            //(1) 不允许用户审核自己的项目
            this.checkUserProcessMyItem();
            //(2) 角色菜单表多余数据
            this.checkRoleMenu();
            //(3) 好友表错误数据
            this.checkFriend(friendList, userList);

//  ==========================5.根据创建时间更新数据id===============================
            //(1)项目表
//            itemMapper.delete(new LambdaQueryWrapper<>());
//            projectMapper.delete(new LambdaQueryWrapper<>());
//            processMapper.delete(new LambdaQueryWrapper<>());
//            scoreMapper.delete(new LambdaQueryWrapper<>());
//            itemList = itemList.stream().sorted(Comparator.comparing(Item::getCreateTime)).collect(Collectors.toList());
//            Map<Integer, Integer> map = new HashMap<>();
//            for (int i = 0; i < itemList.size(); i++) {
//                Item item = itemList.get(i);
//                item.setId(i + 1);
//                map.put(item.getProjectId(), i + 1);
//                item.setProjectId(i + 1);
//                LocalDateTime updateTime = item.getUpdateTime();
//                LocalDateTime createTime = item.getCreateTime();
//                if (updateTime.isBefore(createTime))
//                    item.setUpdateTime(createTime);
//
//                itemList.set(i, item);
//            }
//            projectList = projectList.stream().peek(o -> o.setId(map.get(o.getId()))).collect(Collectors.toList());
//            projectMapper.insertProjectList(projectList);
//            itemMapper.insertItemList(itemList);
//            scoreList = scoreList.stream().peek(o -> o.setProjectId(map.get(o.getProjectId()))).collect(Collectors.toList());
//            for (int i = 0; i < scoreList.size(); i++) {
//                Score score = scoreList.get(i);
//                score.setId(i + 1);
//                scoreList.set(i, score);
//            }
//            scoreMapper.insertScoreList(scoreList);
//            processList = processList.stream().peek(o -> o.setProjectId(map.get(o.getProjectId()))).collect(Collectors.toList());
//            for (int i = 0; i < processList.size(); i++) {
//                Process process = processList.get(i);
//                process.setId(i + 1);
//                processList.set(i, process);
//            }
//            processMapper.insertProcessList(processList);
        }

//  =========================6.创建数据===================================
        //创建好友表数据
            this.createFriendList();
        //创建评分表数据
            this.createScoreList();
        //创建审核表数据
            this.createProcessList();
    }

    /**
     * 检查用户是否审核自己的项目
     */
    private void checkUserProcessMyItem() {
        List<Integer> processIds = processMapper.checkUserProcessMyItem();
        if (StringUtils.isNotEmpty(processIds)) {
            LambdaQueryWrapper<Process> lqw = new LambdaQueryWrapper<>();
            lqw.in(Process::getUserId, processIds);
            processMapper.delete(lqw);
            System.out.println("存在审核自己项目的数据，已删除");
        }
//        Map<Integer, List<Item>> item_groupBy_createBy = itemList.stream().collect(Collectors.groupingBy(Item::getCreateBy));
//        List<Process> ProcessLists_Error = new ArrayList<>();
//        List<Map.Entry<Integer, List<Item>>> list1 = item_groupBy_createBy.entrySet().stream().peek(o -> {
//            Integer id = o.getKey();
//            List<Item> list = o.getValue().stream().peek(l -> {
//                LambdaQueryWrapper<Process> lqw = new LambdaQueryWrapper<>();
//                lqw.eq(Process::getUserId, id);
//                lqw.eq(Process::getProjectId, l.getProjectId());
//                if (processMapper.exists(lqw)) {
//                    Process process = new Process();
//                    process.setUserId(id);
//                    process.setProjectId(l.getProjectId());
//                    ProcessLists_Error.add(process);
//                }
//            }).collect(Collectors.toList());
//        }).collect(Collectors.toList());
//        if (StringUtils.isNotEmpty(ProcessLists_Error)) {
//            processMapper.deleteList(ProcessLists_Error);
//            System.out.println("存在审核自己项目的数据，已删除");
//        }
    }

    /**
     * 检查好友表
     */
    private void checkFriend(List<Friend> friendList, List<User> userList) {
        //a和b相同
        List<Friend> friendDeleteList = friendList.stream().filter(o -> Objects.equals(o.getUserA(), o.getUserB()) || Objects.equals(o.getUserIdA(), o.getUserIdB())).collect(Collectors.toList());
        //a或b不在user表中存在
        List<Integer> ids = userList.stream().map(User::getId).collect(Collectors.toList());
        friendDeleteList.addAll(friendList.stream().filter(o -> (!ids.contains(o.getUserIdA()) || !ids.contains(o.getUserIdB()))).collect(Collectors.toList()));
        //字段为空
        friendDeleteList.addAll(friendList.stream().filter(o -> (StringUtils.isEmpty(o.getUserIdA()) || StringUtils.isEmpty(o.getUserIdB()) || StringUtils.isEmpty(o.getUserA()) || StringUtils.isEmpty(o.getUserB()) || StringUtils.isEmpty(o.getStatus()))).collect(Collectors.toList()));
        //a和b互相关系不同
        friendList.stream().filter(o -> StringUtils.isNotEmpty(o.getUserIdA()) && StringUtils.isNotEmpty(o.getUserIdB())).peek(f -> friendList.stream().filter(o -> StringUtils.isNotEmpty(o.getUserIdA()) && StringUtils.isNotEmpty(o.getUserIdB())).peek(o -> {
            if (f.getUserIdA().equals(o.getUserIdB()) && f.getUserIdB().equals(o.getUserIdA()) && !f.getStatus().equals(o.getStatus())) {
                friendDeleteList.add(f);
                friendDeleteList.add(o);
            }
        }).collect(Collectors.toList())).collect(Collectors.toList());
        //a为b的好友，而b不是a的好友
        friendList.stream().filter(o -> o.getStatus().equals(2)).peek(o -> {
            List<Friend> list = friendList.stream().filter(f -> o.getUserIdA().equals(f.getUserIdB()) && o.getUserIdB().equals(f.getUserIdA())).collect(Collectors.toList());
            if (StringUtils.isEmpty(list)) {
                friendDeleteList.add(o);
            }
        }).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(friendDeleteList)) {
            List<Integer> friendIds = friendDeleteList.stream().map(Friend::getId).collect(Collectors.toList());
            LambdaQueryWrapper<Friend> lqw = new LambdaQueryWrapper<>();
            lqw.in(Friend::getId, friendIds);
            friendMapper.delete(lqw);
            System.out.println("好友表存在错误数据，已删除");
        }
        //a和b互相添加（status为1）
        List<Friend> friendUpdateList = new ArrayList<>();
        friendList.stream().filter(o -> o.getStatus() == 1).peek(o -> {
            List<Friend> list = friendList.stream().filter(i -> i.getStatus() == 1).filter(i -> (StringUtils.isNotEmpty(o.getUserA()) && StringUtils.isNotEmpty(o.getUserB()) && StringUtils.isNotEmpty(i.getUserA()) && StringUtils.isNotEmpty(i.getUserB()) && i.getUserA().equals(o.getUserB()) && i.getUserB().equals(o.getUserA()))).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(list)) {
                friendUpdateList.addAll(list);
            }
        }).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(friendUpdateList)) {
            friendUpdateList.forEach(o -> o.setStatus(2));
            friendMapper.friendUpdateList(friendUpdateList);
            System.out.println("好友表存在异常数据，已更改");
        }
    }

    /**
     * 检查角色菜单表是否存在多余数据
     */
    public void checkRoleMenu() {
        List<RoleMenu> menus = roleMenuMapper.selectList(new LambdaQueryWrapper<>());
        if (StringUtils.isNotEmpty(menus)) {
            List<RoleMenu> inserts = menus.stream().filter(o -> "INSERT".equals(o.getRelationship())).collect(Collectors.toList());
            List<RoleMenu> deletes = menus.stream().filter(o -> "DELETE".equals(o.getRelationship())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(inserts) || StringUtils.isNotEmpty(deletes)) {
                List<Menu> menuList = menuMapper.getMenuList();

                //检查权限本有可以访问菜单，却又添加了数据
                if (StringUtils.isNotEmpty(inserts) && menuList != null) {
                    List<RoleMenu> insertList = inserts.stream().filter(o -> StringUtils.isNotEmpty(menuList.stream().filter(menu -> Objects.equals(o.getMenuId(), menu.getId()) && menu.getAccess() >= o.getAccess()
                            ).collect(Collectors.toList())
                    )).collect(Collectors.toList());

                    //删除数组
                    if (StringUtils.isNotEmpty(insertList)) {
                        roleMenuMapper.deleteList(insertList);
                    }
                }

                //检查权限本就不可以访问菜单，却又添加了数据
                if (StringUtils.isNotEmpty(deletes) && menuList != null) {
                    List<RoleMenu> deleteList = inserts.stream().filter(o -> StringUtils.isNotEmpty(menuList.stream().filter(menu -> Objects.equals(o.getMenuId(), menu.getId()) && menu.getAccess() < o.getAccess()
                            ).collect(Collectors.toList())
                    )).collect(Collectors.toList());

                    //删除数组
                    if (StringUtils.isNotEmpty(deleteList)) {
                        roleMenuMapper.deleteList(deleteList);
                    }
                }
            }

        }
    }

    /**
     * 创建好友表数据
     */
    public void createFriendList() {
        if (!createFriendList) {
            return;
        }
        //开始id
        int begin = 1;
        //能生成的最大数量
        int max = 0;
        //达到最大生成数量的次数,次数大于三认为无法在生成额外的数据
        int max_num = 0;
        //达到最大生成数量的次数
        int max_num_num = 3;
        //删除好友表所有数据
        friendMapper.delete(new LambdaQueryWrapper<>());
        List<User> userList = userMapper.getUserList();

        //创建数量
        int num = (userList.size() - 1) * userList.size();
        //数组分隔时达到的尺寸
        int splitSize = num / 10;
        List<Friend> friendList = new ArrayList<>();
        while (friendList.size() < num && max_num < max_num_num) {
            //创建数据
            for (int i = begin; i <= num; i++) {
                Friend friend = new Friend();
                int IdA = new Random().nextInt(userList.size());
                User user = userList.get(IdA);
                friend.setUserA(user.getAccount());
                friend.setUserIdA(user.getId());
                int IdB = new Random().nextInt(userList.size());
                while (IdA == IdB) {
                    IdB = new Random().nextInt(userList.size());
                }
                User user1 = userList.get(IdB);
                friend.setUserB(user1.getAccount());
                friend.setUserIdB(user1.getId());
                Random status = new Random();
                friend.setStatus(status.nextInt(2) + 1);
                friendList.add(friend);
            }
            //去重
            friendList = friendList.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(o -> o.getUserIdA() + o.getUserIdB()))), ArrayList::new));
            //A->B:0  pass
            //A->B:1  pass
            //A->B:2  A和B互换
            List<Friend> Status2 = friendList.stream().filter(o -> 2 == o.getStatus()).collect(Collectors.toList());
            List<Friend> friends = Status2.stream().map(o -> {
                int idA = o.getUserIdA();
                int idB = o.getUserIdB();
                String A = o.getUserA();
                String B = o.getUserB();
                Friend friend = new Friend();
                friend.setUserIdB(idA);
                friend.setUserB(A);
                friend.setUserIdA(idB);
                friend.setUserA(B);
                friend.setStatus(2);
                return friend;
            }).collect(Collectors.toList());
            friendList.addAll(friends);
            int size = friendList.size();
            if (size == max) {
                max_num++;
            }
            if (size > max) {
                max = size;
                max_num = 0;
            }
        }
        //给列表每一项附上ID
        for (int i = 0; i < friendList.size(); i++) {
            friendList.get(i).setId(i + 1);
        }
        List<List<Friend>> lists = StringUtils.splitList(friendList, splitSize);
        lists.forEach(list -> friendMapper.insertFriendListId(list));
    }

    /**
     * 创建评分表数据
     */
    public void createScoreList() {
        if (!createScoreList) {
            return;
        }
        scoreMapper.delete(new LambdaQueryWrapper<>());
        //开始id
        int begin = 1;
        //能生成的最大数量
        int max = 0;
        //达到最大生成数量的次数,次数大于三认为无法在生成额外的数据
        int max_num = 0;
        //达到最大生成数量的次数
        int max_num_num = 10;
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<>());
        List<Item> items = itemMapper.selectList(new LambdaQueryWrapper<>());
        List<Score> listList = new ArrayList<>();

        //创建数量
        int num = (users.size() - 1) * items.size();
        //数组分隔时达到的尺寸
        int splitSize = num / 10;
        while (max_num <= max_num_num && listList.size() < num) {
            for (int i = begin; i < num; i++) {
                Score score = new Score();
                int userId = users.get(new Random().nextInt(users.size())).getId();
                score.setUserId(userId);
                Item item = items.get(new Random().nextInt(items.size()));
                while (item.getCreateBy() == userId) {
                    item = items.get(new Random().nextInt(items.size()));
                }
                score.setProjectId(item.getProjectId());
                score.setScore(new Random().nextInt(10) * 1.0);
                listList.add(score);
            }
            //去重
            listList = listList.stream().collect(Collectors.collectingAndThen(
                    Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(o -> o.getUserId() + o.getProjectId()))), ArrayList::new));
            int size = listList.size();
            if (size == max) {
                max_num++;
            }
            if (size > max) {
                max = size;
                max_num = 0;
            }

        }
        for (int i = 0; i < listList.size(); i++) {
            listList.get(i).setId(i + 1);
        }
        List<List<Score>> lists = StringUtils.splitList(listList, splitSize);
        lists.forEach(list -> scoreMapper.insertScoreList(list));
    }

    /**
     * 创建审核表数据
     */
    public void createProcessList() {
        if (!createProcessList) {
            return;
        }
        processMapper.delete(new LambdaQueryWrapper<>());
        //开始id
        int begin = 1;
        //能生成的最大数量
        int max = 0;
        //达到最大生成数量的次数,次数大于三认为无法在生成额外的数据
        int max_num = 0;
        //达到最大生成数量的次数
        int max_num_num = 10;
        List<User> users = userMapper.selectList(new LambdaQueryWrapper<>());
        List<Item> items = itemMapper.selectList(new LambdaQueryWrapper<>());
        List<Process> list = new ArrayList<>();

        //创建数量
        int num = (users.size() - 1) * items.size();
        //数组分隔时达到的尺寸
        int splitSize = num / 10;
        String[] process_result = new String[]{"未审核", "审核未通过", "审核通过"};
        while (max_num <= max_num_num && list.size() < num) {
            for (int i = begin; i < num; i++) {
                Process process = new Process();
                int userId = users.get(new Random().nextInt(users.size())).getId();
                process.setUserId(userId);
                Item item = items.get(new Random().nextInt(items.size()));
                while (item.getCreateBy() == userId) {
                    item = items.get(new Random().nextInt(items.size()));
                }
                process.setProjectId(item.getProjectId());
                int i_Process = new Random().nextInt(2);
                if (i_Process == 0) {
                    // 0 or 1
                    //未审核
                    process.setIsProcess("NO");
                } else {
                    //审核过
                    i_Process = new Random().nextInt(2) + 1;
                    process.setIsProcess("YES");
                }
                process.setProcess(process_result[i_Process]);
                process.setRemark(process_result[i_Process]);
                list.add(process);
            }
            //去重
            list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUserId() + ":" + o.getProjectId()))), ArrayList::new));
            int size = list.size();
            if (size == max) {
                max_num++;
            }
            if (size > max) {
                max = size;
                max_num = 0;
            }
            System.out.println("创建审核表：num条".replace("num", list.size() + ""));
        }
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setId(i + 1);
        }
        List<List<Process>> lists = StringUtils.splitList(list, splitSize);
        lists.forEach(process -> processMapper.insertProcessList(process));

    }
}

package com.example.system.service.impl;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.TokenSign;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.SystemConstant;
import com.example.common.response.R;
import com.example.common.utils.ExcelUtils;
import com.example.common.utils.GzipUtils;
import com.example.common.utils.StringUtils;
import com.example.common.webSocket.SendSocket;
import com.example.common.webSocket.WebSocket;
import com.example.system.domain.bo.PageBo;
import com.example.system.domain.bo.UserBo;
import com.example.system.domain.entity.Process;
import com.example.system.domain.entity.*;
import com.example.system.domain.model.LoginTempInfo;
import com.example.system.domain.model.Notify;
import com.example.system.domain.vo.PageVo;
import com.example.system.domain.vo.UserVo;
import com.example.system.mapper.*;
import com.example.system.others.utils.CheckUtils;
import com.example.system.service.ItemService;
import com.example.system.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ActivationMapper activationMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private ProcessMapper processMapper;
    @Autowired
    private GzipUtils gzipUtils;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private UserPermMapper userPermMapper;
    @Autowired
    private WebSocket webSocket;

    /**
     * 搜索 用户/账户 列表-分页
     */
    @Override
    public PageVo getPageVo(PageBo pageBo) {
        List<UserVo> list = this.getList();
        return new PageVo(pageBo, list);
    }

    /**
     * 列表-不分页
     */
    @Override
    public List<UserVo> getList() {
        return userMapper.getUserListVo();
    }

    /**
     * 模糊查询用户列表
     * 查询账号列、姓名列、用户名列、账号权限列(角色名称)、邮箱列、手机号列
     */
    @Override
    public R<PageVo> getPageVoSearch(UserBo userBo, PageBo pageBo) {
        List<UserVo> userList = this.getList();

        String username = userBo.getUsername();
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(userList)) {
            userList = userList.stream().filter(o ->StringUtils.isNotEmpty(o.getUsername()) && o.getUsername().contains(username)).collect(Collectors.toList());
        }

        String account = userBo.getAccount();
        if (StringUtils.isNotEmpty(account) && StringUtils.isNotEmpty(userList)) {
            userList = userList.stream().filter(o -> StringUtils.isNotEmpty(o.getAccount()) && o.getAccount().contains(account)).collect(Collectors.toList());
        }

        String name = userBo.getName();
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(userList)) {
            userList = userList.stream().filter(o ->StringUtils.isNotEmpty(o.getName()) && o.getName().contains(name)).collect(Collectors.toList());
        }

        String email = userBo.getEmail();
        if (StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(userList)) {
            userList = userList.stream().filter(o ->StringUtils.isNotEmpty(o.getEmail()) && o.getEmail().contains(email)).collect(Collectors.toList());
        }

        String mobile = userBo.getMobile();
        if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(userList)) {
            userList = userList.stream().filter(o ->StringUtils.isNotEmpty(o.getMobile()) && o.getMobile().contains(mobile)).collect(Collectors.toList());
        }

        String status = userBo.getStatus();
        if (StringUtils.isNotEmpty(status) && StringUtils.isNotEmpty(userList)) {
            userList = userList.stream().filter(o ->StringUtils.isNotEmpty(o.getStatus()) && o.getStatus().equals(status)).collect(Collectors.toList());
        }

        String sex = userBo.getSex();
        if (StringUtils.isNotEmpty(sex) && StringUtils.isNotEmpty(userList)) {
            userList = userList.stream().filter(o ->StringUtils.isNotEmpty(o.getSex()) && o.getSex().equals(sex)).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(userList)) {
            return R.ok(new PageVo(pageBo, userList));
        }
        return R.info("没有符合条件的数据", new PageVo());
    }

    /**
     * 获取 用户/账户 头像，并转为二进制数组
     */
    @Override
    public byte[] getUserImage(String account) {
        int imageId = userMapper.getUserByAccount(account).getImageId();
        String image = imageMapper.selectById(imageId).getImage();
        return gzipUtils.compress(image);
    }

    /**
     * 回显个人信息
     * 1.用户名 username
     * 2.角色名称 roleName
     * 3.角色ID roleId
     * 4.上次登录事件 time
     * 5.上次登录IP ip
     * 6.头像 image
     * 7.手机号码 mobile
     * 8.邮箱 email
     * 9.账号创建时间 createTime
     */
    @Override
    public Map<String, Object> getInfo() {
        String account = (String) StpUtil.getLoginId();
        User user = userMapper.getUserByAccount(account);
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("mobile", user.getMobile());
        map.put("email", user.getEmail());
        map.put("sex", user.getSex());
        map.put("createTime", user.getCreateTime().toString().replace("T", " "));
        Role role = roleMapper.getRoleByAccount(account);
        map.put("roleName", role.getRoleName());
        map.put("roleId", role.getId());
        LambdaQueryWrapper<Login> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Login::getAccount, account);
        List<Login> loginList = loginMapper.selectList(lqw);
        loginList = loginList.stream().sorted(Comparator.comparing(Login::getTime).reversed()).collect(Collectors.toList());
        if(StringUtils.isNotEmpty(loginList)) {
            Login login = loginList.get(0);
            map.put("time", login.getTime().toString().replace("T", " "));
            map.put("ip", login.getIp());
        }
        byte[] image = getUserImage(account);
        map.put("image", image);
        return map;
    }

    /**
     * 修改账号状态
     */
    @Override
    @SneakyThrows
    public String changeStatus(UserBo bo) {
        User user = userMapper.selectById(bo.getId());
        String status = user.getStatus();
        String normal = SystemConstant.USER_STATUS_NORMAL;
        String unnormal = SystemConstant.USER_STATUS_DISABLE;
        status = status.equals(normal) ? unnormal : normal;
        user.setStatus(status);
        userMapper.updateById(user);
        if (status.equals(normal)) {
            return "激活成功";
        } else {
            return "禁用成功";
        }
    }

    /**
     * 批量删除
     */
    @Override
    public String deleteList(List<Integer> list) {
        list.forEach(id->{
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getId,id);
            User user = userMapper.selectOne(lqw);
            String account = user.getAccount();
            //删除用户表的关联表
            //1.用户权限表 sys_user_perm
            LambdaQueryWrapper<UserPerm> lqwUserPerm = new LambdaQueryWrapper<>();
            lqwUserPerm.eq(UserPerm::getUserId, id);
            userPermMapper.delete(lqwUserPerm);
            //2.项目内容表 sys_Project和项目表 sys_Item
            LambdaQueryWrapper<Item> lqwItem = new LambdaQueryWrapper<>();
            lqwItem.eq(Item::getCreateBy, id);
            List<Item> items = itemMapper.selectList(lqwItem);
            if (!items.isEmpty()) {
                itemMapper.delete(lqwItem);
                LambdaQueryWrapper<Project> lqwProject = new LambdaQueryWrapper<>();
                for (Item i : items) {
                    lqwProject.eq(Project::getId, i.getProjectId());
                }
                projectMapper.delete(lqwProject);
                itemService.updateItemsRedis();
            }
            //3.用户活跃表 sys_activation
            LambdaQueryWrapper<Activation> lqwActivation = new LambdaQueryWrapper<>();
            lqwActivation.eq(Activation::getUserId, id);
            activationMapper.delete(lqwActivation);
            //4.评分表 sys_score
            LambdaQueryWrapper<Score> lqwScore = new LambdaQueryWrapper<>();
            lqwScore.eq(Score::getUserId, id);
            scoreMapper.delete(lqwScore);
            //5.好友关系表 sys_friend
            LambdaQueryWrapper<Friend> lqwFriend1 = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Friend> lqwFriend2 = new LambdaQueryWrapper<>();
            lqwFriend1.eq(Friend::getUserIdA, id);
            lqwFriend2.eq(Friend::getUserIdB, id);
            friendMapper.delete(lqwFriend1);
            friendMapper.delete(lqwFriend2);
            //6.用户审核表 sys_process
            LambdaQueryWrapper<Process> lqwProcess = new LambdaQueryWrapper<>();
            lqwProcess.eq(Process::getUserId, id);
            processMapper.delete(lqwProcess);
            //7.日志表 sys_log
            LambdaQueryWrapper<Log> lqwLog = new LambdaQueryWrapper<>();
            lqwLog.eq(Log::getAccount, account);
            logMapper.delete(lqwLog);
            //8.登录记录表 sys_login
            LambdaQueryWrapper<Login> lqwLogin = new LambdaQueryWrapper<>();
            lqwLogin.eq(Login::getAccount, account);
            loginMapper.delete(lqwLogin);
            //9.用户角色表 sys_user_role
            LambdaQueryWrapper<UserRole> lqwUserRole = new LambdaQueryWrapper<>();
            lqwUserRole.eq(UserRole::getUserId, id);
            userRoleMapper.delete(lqwUserRole);
            //10.删除用户表信息 sys_user
            LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
            lqwUser.eq(User::getId, id);
            userMapper.delete(lqwUser);
        });
        return "删除成功";
    }

    /**
     * 新增用户
     */
    @Override
    @SneakyThrows
    public String  insertUser(User user) {
        this.checkUserBeforeInsert(user);
        //校验没问题
        user.setStatus(SystemConstant.USER_STATUS_NORMAL);
        //用户输入的密码
        String password = user.getPassword();
        //生成随机加密盐值
        String salt = IdUtil.simpleUUID();
        password = SaSecureUtil.sha256(password + salt);
        user.setSalt(salt);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        user.setStatus(SystemConstant.USER_STATUS_NORMAL);
        //给用户生成一张照片
        user.setImageId(SystemConstant.IMAGE_ID);
        //添加用户信息
        userMapper.insertUser(user);
        //生成账号和id,并更新
        int userId = user.getId();
        String account = String.valueOf(userId + SystemConstant.BILLION - 1);
        user.setAccount(account);
        user.setId(userId);
        userMapper.updateById(user);

        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        Integer roleId;
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Role::getAccess);
        lqw.last("limit 1");
        Role role = roleMapper.selectOne(lqw);
        if (StringUtils.isNotEmpty(role)) {
            roleId = role.getId();
        } else {
            throw new Exception("添加用户失败！无角色可以选择！");
        }
        userRole.setRoleId(roleId);
        userRoleMapper.insertUserRole(userRole);

        if (StpUtil.isLogin()) {
            SendSocket socket = new SendSocket();
            //返回notify提示
            socket.setType("notify");
            socket.setMessage("");

            Notify notify = new Notify();
            notify.setDuration(0);
            notify.setTitle("账号注册成功！");
            notify.setMessage("请记住您的账号：" + account);
            notify.setColor("red");
            notify.setIcon("user-solid");

            socket.setData(notify);

            webSocket.sendOneMessage((String) StpUtil.getLoginId(), socket);
        }
        return "添加成功";
    }

    /**
     * 修改密码
     */
    @Override
    public R<String> updatePwd(UserBo bo) {
        String account = (String) StpUtil.getLoginId();
        User user = userMapper.getUserByAccount(account);
        String salt = user.getSalt();
        String pwd = SaSecureUtil.sha256(bo.getOldPassword() + salt);
        if (!pwd.equals(user.getPassword())) {
            return R.fail("修改密码失败，旧密码不正确");
        }
        pwd = SaSecureUtil.sha256(bo.getPassword() + salt);
        userMapper.updatePwd(user.getAccount(), pwd);
        return R.ok("修改密码成功");
    }

    /**
     * 强制退出
     */
    @Override
    public PageVo getOnlineList(PageBo pageBo) {
        StpLogic stpLogic = SaManager.getStpLogic("login");
        List<String> tokens = stpLogic.searchTokenValue("", -1, -1, true);
        // 保存所有用户id及对应的不同平台下的临时有效时长
        List<LoginTempInfo> list = new ArrayList<>();
        tokens.forEach(o -> {
            //真实token
            String token = o.substring(o.lastIndexOf(":") + 1);
            //token过期时间
            long loginTimeout = stpLogic.getTokenActivityTimeoutByToken(token);
            if (loginTimeout == -2) {
                // 当前 token 所代表的会话已经临时过期了, 直接跳过
                return;
            }
            //从token获取登录的账号
            String account = (String) stpLogic.getLoginIdByToken(token);
            String username = userMapper.getUserByAccount(account).getUsername();
            // 根据登录id和token, 获取对应的登录类型
            String mobile = null;
            SaSession session = stpLogic.getSessionByLoginId(account, false);
            if (session == null) {
                mobile = "";
            } else {
                // 遍历解析
                List<TokenSign> tokenSignList = session.tokenSignListCopy();
                for (TokenSign tokenSign : tokenSignList) {
                    if (tokenSign.getValue().equals(token)) {
                        mobile = tokenSign.getDevice();
                        break;
                    }
                }
            }
            // 每个用户id可以多次登录, 也可以在不同平台登录
            LoginTempInfo tempInfo = new LoginTempInfo();
            tempInfo.setAccount(account);
            tempInfo.setUsername(username);
            tempInfo.setToken(token);
            tempInfo.setMobile(mobile);
            tempInfo.setLoginTimeout(loginTimeout);
            // 同一个用户如果多端登录需要同时记录不同平台的时限
            list.add(tempInfo);
        });
        return new PageVo(pageBo, list);
    }

    @Override
    public void forcedOut(String account) {
        SendSocket socket = new SendSocket();
        socket.setType("forcedOut");
        socket.setMessage("您已被强制退出，请重新登陆或者联系管理员。");

        WebSocket webSocket = new WebSocket();
        webSocket.sendOneMessage(account, socket);
        StpUtil.logout(account);
    }

    /**
     * @param paths 文件地址
     */
    @Override
    public R<String> importUser(List<String> paths) {
        List<User> lists = new ArrayList<>();
        paths.forEach(path -> lists.addAll(ExcelUtils.parseFromExcel(path, User.class)));
        if(StringUtils.isEmpty(lists)){
            return R.warn("导入失败，文件中无数据！");
        }
        List<String> list1 = userMapper.selectList(new LambdaQueryWrapper<>()).stream().map(User::getAccount).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<User> list2 = lists.stream().filter(o -> !list1.contains(o.getAccount())).collect(Collectors.toList());
        if (StringUtils.isEmpty(list2)) {
            return R.warn("导入失败，文件中存在重复数据！");
        }
        this.insertUserList(list2);
        return R.ok("导入成功，共计" + list2.size() + "条数据！");
    }

    /**
     * 更改账号信息
     */
    @Override
    public String updateUser(UserBo userBo) {
        this.checkUserBeforeUpdate(userBo);
        userMapper.updateById(userBo);

        //修改用户角色
        LambdaQueryWrapper<UserRole> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserRole::getUserId, userBo.getId());
        UserRole userRole = userRoleMapper.selectOne(lqw);

        userRole.setRoleId(userBo.getRoleId());
        if (StringUtils.isNotEmpty(userRole)) {
            //存在
            if(!userRole.getRoleId().equals(userBo.getRoleId())) {
                //角色不相等
                userRoleMapper.updateById(userRole);
            }
        }else {
            //不存在
            userRole.setUserId(userBo.getId());
            userRoleMapper.insertUserRole(userRole);
        }
        return "修改成功";
    }

    /**
     * 个人中心-更改个人信息
     * 用户名称，手机号码，邮箱，性别
     */
    @Override
    public R<String> updateUserInfo(UserBo bo) {
        String account = (String) StpUtil.getLoginId();
        Integer id = userMapper.getUserByAccount(account).getId();
        bo.setId(id);
        userMapper.updateById(bo);
        return R.ok("修改成功");
    }

    private void insertUserList(List<User> userList) {
        userList.forEach(user -> {
            //用户输入的密码
            String password = user.getPassword();
            //生成随机加密盐值
            String salt = IdUtil.simpleUUID();
            password = SaSecureUtil.sha256(password + salt);
            user.setSalt(salt);
            user.setPassword(password);
            user.setCreateTime(LocalDateTime.now());
            user.setStatus(SystemConstant.USER_STATUS_NORMAL);
            //给用户生成一张照片
            user.setImageId(SystemConstant.IMAGE_ID);
            //添加用户信息
            userMapper.insertUser(user);
            //生成账号和id,并更新
            int userId = user.getId();
            String account = String.valueOf(userId + SystemConstant.BILLION - 1);
            user.setAccount(account);
            user.setId(userId);
            userMapper.updateById(user);

            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            Integer roleId = user.getRoleId();
            if(StringUtils.isEmpty(roleId)) {
                List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<>()).stream().sorted(Comparator.comparingInt(Role::getAccess).reversed()).collect(Collectors.toList());
                if(StringUtils.isNotEmpty(roles)){
                    Role role = roles.get(0);
                    roleId = role.getId();
                    userRole.setRoleId(roleId);
                    userRoleMapper.insertUserRole(userRole);
                }
            }

        });
    }

    /**
     * 手机号码正则 验证手机号码格式
     * true 格式正确
     * 添加用户前，检查用户数据
     */
    @SneakyThrows
    private void checkUserBeforeInsert(User user) {
        this.checkUser(user);

        //检查密码
        String pwd = user.getPassword();
        if (StringUtils.isEmpty(pwd)) {
            throw new Exception("数据检查出现异常：密码为空");
        }
        if (pwd.length() < 8) {
            throw new Exception("数据检查出现异常：密码必须大于8位");
        }
        if (!CheckUtils.checkContainDigit(pwd) || !CheckUtils.checkContainCase(pwd) || !CheckUtils.checkContainSpecialChar(pwd)) {
            throw new Exception("密码必须包含数字、大小写字母、特殊符号");
        }
    }

    /**
     * 修改用户前，检查用户数据
     */
    @SneakyThrows
    private void checkUserBeforeUpdate(UserBo user) {
        this.checkUser(user);

        //检查账号角色
        if (StringUtils.isEmpty(user.getRoleId())) {
            throw new Exception("数据检查出现异常：角色不能为空");
        }
    }

    /**
     * 通用检查
     */
    @SneakyThrows
    private void checkUser(User user) {
        //检查用户名
        String username = user.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new Exception("数据检查出现异常：用户名为空");
        }
        if (!(username.length() > 1 && username.length() <= 20)) {
            throw new Exception("数据检查出现异常：用户名的长度应为1到20个字符");
        }
        //不做用户名唯一校验，允许用户名重复

        //检查姓名
        String name = user.getName();
        if (StringUtils.isEmpty(name)) {
            throw new Exception("数据检查出现异常：姓名为空");
        }
        if (!(name.length() > 1 && name.length() <= 20)) {
            throw new Exception("数据检查出现异常：姓名的长度应为1到20个字符");
        }
        //不做姓名唯一校验，允许姓名重复

        //校验手机号
        checkMobile(user.getMobile());
        //校验邮箱
        checkEmail(user.getEmail());
    }

    /**
     * 手机号码正则 验证手机号码格式
     * true 格式正确
     */
    @SneakyThrows
    private void checkMobile(String mobile) {
        if (mobile.length() != 11 || StringUtils.isEmpty(mobile)) {
            throw new Exception("数据检查出现异常：手机号码长度需要为11位");
        }
        if (!Pattern.compile("^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16[56])|(17[0-8])|(18[0-9])|(19[1589]))\\d{8}$", Pattern.CASE_INSENSITIVE).matcher(mobile).matches()) {
            throw new Exception("数据检查出现异常：手机号码格式不正确");
        }
    }

    /**
     * 邮箱正则 验证邮箱格式
     * true 格式正确
     */
    @SneakyThrows
    private void checkEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new Exception("数据检查出现异常：邮箱不能为空");
        }
        if (!Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", Pattern.CASE_INSENSITIVE).matcher(email).matches()) {
            throw new Exception("数据检查出现异常：邮箱格式不正确");
        }
    }
}

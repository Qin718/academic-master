-- 活跃度表
CREATE TABLE `sys_activation` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户Id',
  `all_activation` double(11, 0) NULL DEFAULT 0 COMMENT '总活跃度',
  `page_view` double(11, 0) NULL DEFAULT 0 COMMENT '浏览活跃度',
  `add_item` double(11, 0) NULL DEFAULT 0 COMMENT '创建项目活跃度',
  `score_project` double(11, 0) NULL DEFAULT 0 COMMENT '评分活跃度',
  `login` double(11, 0) NULL DEFAULT 0 COMMENT '登录活跃度',
  `process` double NULL DEFAULT 0 COMMENT '审核项目活跃度',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
);

-- 参数表
CREATE TABLE `sys_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config` varchar(100) NULL DEFAULT NULL COMMENT '标识符',
  `value` varchar(255) NULL DEFAULT NULL COMMENT '参数内容',
  `remark` varchar(255) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
);

-- 字典表
CREATE TABLE `sys_dict` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(10) NULL DEFAULT NULL COMMENT '字典类型',
  `name` varchar(10) NULL DEFAULT NULL COMMENT '字典名字',
  `dict` varchar(200) NULL DEFAULT NULL COMMENT '字典内容',
  PRIMARY KEY (`id`)
);

-- 附件表
CREATE TABLE `sys_file` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NULL DEFAULT NULL COMMENT '文件名称',
  `type` varchar(100) NULL DEFAULT NULL COMMENT '文件类型', 
  `stream` longtext NULL COMMENT '文件数据流',
  `project_id` int NULL DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 好友关系表
CREATE TABLE `sys_friend` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id_a` int NULL DEFAULT NULL COMMENT '用户A的id',
  `user_id_b` int NULL DEFAULT NULL COMMENT '用户B的id',
  `user_a` varchar(50) NULL DEFAULT NULL COMMENT '用户A的账号',
  `user_b` varchar(50) NULL DEFAULT NULL COMMENT '用户B的账号',
  `status` varchar(5) NULL DEFAULT '1' COMMENT 'A向B发起好友申请，流程状态（1.默认，2.同意，3.拒绝）',
  `notes` varchar(50) NULL DEFAULT NULL COMMENT 'A对B的备注',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id_a`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`user_id_b`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`user_a`) REFERENCES `sys_user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`user_b`) REFERENCES `sys_user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 菜单表
CREATE TABLE `sys_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int NULL DEFAULT NULL COMMENT '父菜单ID',
  `name` varchar(50) NULL DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(200) NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(255) NULL DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(100) NULL DEFAULT NULL COMMENT '菜单图标',
  `type` int NULL DEFAULT NULL COMMENT '菜单类型',
  `order_num` int NULL DEFAULT NULL COMMENT '显示顺序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
);

-- 项目表
CREATE TABLE `sys_project` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) NULL DEFAULT NULL COMMENT '项目名称',
  `description` varchar(500) NULL DEFAULT NULL COMMENT '项目描述',
  `user_id` int NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `status` int NULL DEFAULT 1 COMMENT '项目状态(1.待审核,2.审核通过,3.审核未通过)',
  `hot` int NULL DEFAULT 0 COMMENT '是否为热门项目(0.否,1.是)',
  `score` double NULL DEFAULT NULL COMMENT '项目评分',
  `score_num` int NULL DEFAULT 0 COMMENT '评分人数',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
);

-- 项目评分表
CREATE TABLE `sys_project_score` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` int NULL DEFAULT NULL COMMENT '项目id',
  `user_id` int NULL DEFAULT NULL COMMENT '评分人id',
  `score` double NULL DEFAULT NULL COMMENT '评分',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 角色表
CREATE TABLE `sys_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) NULL DEFAULT NULL COMMENT '角色名称',
  `role_key` varchar(100) NULL DEFAULT NULL COMMENT '角色权限字符串',
  `order_num` int NULL DEFAULT NULL COMMENT '显示顺序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
);

-- 角色菜单关联表
CREATE TABLE `sys_role_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 用户表
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(50) NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(100) NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(30) NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(11) NULL DEFAULT NULL COMMENT '手机号码',
  `sex` char(1) NULL DEFAULT NULL COMMENT '用户性别',
  `avatar` varchar(100) NULL DEFAULT NULL COMMENT '头像地址',
  `status` char(1) NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `login_ip` varchar(128) NULL DEFAULT NULL COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`account`)
);

-- 用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 项目日志表
CREATE TABLE `sys_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_by` int NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `project_id` int NULL DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 图片表
CREATE TABLE `sys_image` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `image` longtext NOT NULL COMMENT '图片编码',
  PRIMARY KEY (`id`)
);

-- 登录日志表
CREATE TABLE `sys_login` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(11) NULL DEFAULT NULL COMMENT '账号',
  `username` varchar(100) NULL DEFAULT NULL COMMENT '用户名',
  `time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `ip` varchar(100) NULL DEFAULT NULL COMMENT '登录地址',
  `mobile` varchar(10) NULL DEFAULT NULL COMMENT '访问设备',
  `inner_ip` varchar(10) NULL DEFAULT NULL COMMENT '访问来源',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`account`) REFERENCES `sys_user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE
);
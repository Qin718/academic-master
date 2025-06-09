SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_activation
-- ----------------------------
DROP TABLE IF EXISTS `sys_activation`;
CREATE TABLE `sys_activation`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户Id',
  `all_activation` double(11, 0) NULL DEFAULT 0 COMMENT '总活跃度',
  `page_view` double(11, 0) NULL DEFAULT 0 COMMENT '浏览活跃度',
  `add_item` double(11, 0) NULL DEFAULT 0 COMMENT '创建项目活跃度',
  `score_project` double(11, 0) NULL DEFAULT 0 COMMENT '评分活跃度',
  `login` double(11, 0) NULL DEFAULT 0 COMMENT '登录活跃度',
  `process` double NULL DEFAULT 0 COMMENT '审核项目活跃度',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `活跃度表用户外键`(`user_id` ASC) USING BTREE,
  CONSTRAINT `sys_activation_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活跃度表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_activation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `config` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标识符',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数内容',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数表' ROW_FORMAT = COMPACT;


-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名字',
  `dict` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'type', '字典类型', 'type');
INSERT INTO `sys_dict` VALUES (2, 'type', '菜单字典', 'menuType');
INSERT INTO `sys_dict` VALUES (3, 'type', '图标字典', 'icon');
INSERT INTO `sys_dict` VALUES (4, 'icon', '图标字典', '{\"name\": \"eleme\",\"icon\": \"eleme\"}');
INSERT INTO `sys_dict` VALUES (5, 'icon', '图标字典', '{\"name\": \"delete-solid\",\"icon\": \"delete-solid\"}');
INSERT INTO `sys_dict` VALUES (6, 'icon', '图标字典', '{\"name\": \"delete\",\"icon\": \"delete\"}');
INSERT INTO `sys_dict` VALUES (7, 'icon', '图标字典', '{\"name\": \"s-tools\",\"icon\": \"s-tools\"}');
INSERT INTO `sys_dict` VALUES (8, 'icon', '图标字典', '{\"name\": \"setting\",\"icon\": \"setting\"}');
INSERT INTO `sys_dict` VALUES (9, 'icon', '图标字典', '{\"name\": \"user-solid\",\"icon\": \"user-solid\"}');
INSERT INTO `sys_dict` VALUES (10, 'icon', '图标字典', '{\"name\": \"user\",\"icon\": \"user\"}');
INSERT INTO `sys_dict` VALUES (11, 'icon', '图标字典', '{\"name\": \"phone\",\"icon\": \"phone\"}');
INSERT INTO `sys_dict` VALUES (12, 'icon', '图标字典', '{\"name\": \"phone-outline\",\"icon\": \"phone-outline\"}');
INSERT INTO `sys_dict` VALUES (13, 'icon', '图标字典', '{\"name\": \"more\",\"icon\": \"more\"}');
INSERT INTO `sys_dict` VALUES (14, 'icon', '图标字典', '{\"name\": \"more-outline\",\"icon\": \"more-outline\"}');
INSERT INTO `sys_dict` VALUES (15, 'icon', '图标字典', '{\"name\": \"star-on\",\"icon\": \"star-on\"}');
INSERT INTO `sys_dict` VALUES (16, 'icon', '图标字典', '{\"name\": \"star-off\",\"icon\": \"star-off\"}');
INSERT INTO `sys_dict` VALUES (17, 'icon', '图标字典', '{\"name\": \"s-goods\",\"icon\": \"s-goods\"}');
INSERT INTO `sys_dict` VALUES (18, 'icon', '图标字典', '{\"name\": \"goods\",\"icon\": \"goods\"}');
INSERT INTO `sys_dict` VALUES (19, 'icon', '图标字典', '{\"name\": \"warning\",\"icon\": \"warning\"}');
INSERT INTO `sys_dict` VALUES (20, 'icon', '图标字典', '{\"name\": \"warning-outline\",\"icon\": \"warning-outline\"}');
INSERT INTO `sys_dict` VALUES (21, 'icon', '图标字典', '{\"name\": \"question\",\"icon\": \"question\"}');
INSERT INTO `sys_dict` VALUES (22, 'icon', '图标字典', '{\"name\": \"info\",\"icon\": \"info\"}');
INSERT INTO `sys_dict` VALUES (23, 'icon', '图标字典', '{\"name\": \"remove\",\"icon\": \"remove\"}');
INSERT INTO `sys_dict` VALUES (24, 'icon', '图标字典', '{\"name\": \"circle-plus\",\"icon\": \"circle-plus\"}');
INSERT INTO `sys_dict` VALUES (25, 'icon', '图标字典', '{\"name\": \"success\",\"icon\": \"success\"}');
INSERT INTO `sys_dict` VALUES (26, 'icon', '图标字典', '{\"name\": \"error\",\"icon\": \"error\"}');
INSERT INTO `sys_dict` VALUES (27, 'icon', '图标字典', '{\"name\": \"zoom-in\",\"icon\": \"zoom-in\"}');
INSERT INTO `sys_dict` VALUES (28, 'icon', '图标字典', '{\"name\": \"zoom-out\",\"icon\": \"zoom-out\"}');
INSERT INTO `sys_dict` VALUES (29, 'icon', '图标字典', '{\"name\": \"remove-outline\",\"icon\": \"remove-outline\"}');
INSERT INTO `sys_dict` VALUES (30, 'icon', '图标字典', '{\"name\": \"circle-plus-outline\",\"icon\": \"circle-plus-outline\"}');
INSERT INTO `sys_dict` VALUES (31, 'icon', '图标字典', '{\"name\": \"circle-check\",\"icon\": \"circle-check\"}');
INSERT INTO `sys_dict` VALUES (32, 'icon', '图标字典', '{\"name\": \"circle-close\",\"icon\": \"circle-close\"}');
INSERT INTO `sys_dict` VALUES (33, 'icon', '图标字典', '{\"name\": \"s-help\",\"icon\": \"s-help\"}');
INSERT INTO `sys_dict` VALUES (34, 'icon', '图标字典', '{\"name\": \"help\",\"icon\": \"help\"}');
INSERT INTO `sys_dict` VALUES (35, 'icon', '图标字典', '{\"name\": \"minus\",\"icon\": \"minus\"}');
INSERT INTO `sys_dict` VALUES (36, 'icon', '图标字典', '{\"name\": \"plus\",\"icon\": \"plus\"}');
INSERT INTO `sys_dict` VALUES (37, 'icon', '图标字典', '{\"name\": \"check\",\"icon\": \"check\"}');
INSERT INTO `sys_dict` VALUES (38, 'icon', '图标字典', '{\"name\": \"close\",\"icon\": \"close\"}');
INSERT INTO `sys_dict` VALUES (39, 'icon', '图标字典', '{\"name\": \"picture\",\"icon\": \"picture\"}');
INSERT INTO `sys_dict` VALUES (40, 'icon', '图标字典', '{\"name\": \"picture-outline\",\"icon\": \"picture-outline\"}');
INSERT INTO `sys_dict` VALUES (41, 'icon', '图标字典', '{\"name\": \"picture-outline-round\",\"icon\": \"picture-outline-round\"}');
INSERT INTO `sys_dict` VALUES (42, 'icon', '图标字典', '{\"name\": \"upload\",\"icon\": \"upload\"}');
INSERT INTO `sys_dict` VALUES (43, 'icon', '图标字典', '{\"name\": \"upload2\",\"icon\": \"upload2\"}');
INSERT INTO `sys_dict` VALUES (44, 'icon', '图标字典', '{\"name\": \"download\",\"icon\": \"download\"}');
INSERT INTO `sys_dict` VALUES (45, 'icon', '图标字典', '{\"name\": \"camera-solid\",\"icon\": \"camera-solid\"}');
INSERT INTO `sys_dict` VALUES (46, 'icon', '图标字典', '{\"name\": \"camera\",\"icon\": \"camera\"}');
INSERT INTO `sys_dict` VALUES (47, 'icon', '图标字典', '{\"name\": \"video-camera-solid\",\"icon\": \"video-camera-solid\"}');
INSERT INTO `sys_dict` VALUES (48, 'icon', '图标字典', '{\"name\": \"video-camera\",\"icon\": \"video-camera\"}');
INSERT INTO `sys_dict` VALUES (49, 'icon', '图标字典', '{\"name\": \"message-solid\",\"icon\": \"message-solid\"}');
INSERT INTO `sys_dict` VALUES (50, 'icon', '图标字典', '{\"name\": \"bell\",\"icon\": \"bell\"}');
INSERT INTO `sys_dict` VALUES (51, 'icon', '图标字典', '{\"name\": \"s-cooperation\",\"icon\": \"s-cooperation\"}');
INSERT INTO `sys_dict` VALUES (52, 'icon', '图标字典', '{\"name\": \"s-order\",\"icon\": \"s-order\"}');
INSERT INTO `sys_dict` VALUES (53, 'icon', '图标字典', '{\"name\": \"s-platform\",\"icon\": \"s-platform\"}');
INSERT INTO `sys_dict` VALUES (54, 'icon', '图标字典', '{\"name\": \"s-fold\",\"icon\": \"s-fold\"}');
INSERT INTO `sys_dict` VALUES (55, 'icon', '图标字典', '{\"name\": \"s-unfold\",\"icon\": \"s-unfold\"}');
INSERT INTO `sys_dict` VALUES (56, 'icon', '图标字典', '{\"name\": \"s-operation\",\"icon\": \"s-operation\"}');
INSERT INTO `sys_dict` VALUES (57, 'icon', '图标字典', '{\"name\": \"s-promotion\",\"icon\": \"s-promotion\"}');
INSERT INTO `sys_dict` VALUES (58, 'icon', '图标字典', '{\"name\": \"s-home\",\"icon\": \"s-home\"}');
INSERT INTO `sys_dict` VALUES (59, 'icon', '图标字典', '{\"name\": \"s-release\",\"icon\": \"s-release\"}');
INSERT INTO `sys_dict` VALUES (60, 'icon', '图标字典', '{\"name\": \"s-ticket\",\"icon\": \"s-ticket\"}');
INSERT INTO `sys_dict` VALUES (61, 'icon', '图标字典', '{\"name\": \"s-management\",\"icon\": \"s-management\"}');
INSERT INTO `sys_dict` VALUES (62, 'icon', '图标字典', '{\"name\": \"s-open\",\"icon\": \"s-open\"}');
INSERT INTO `sys_dict` VALUES (63, 'icon', '图标字典', '{\"name\": \"s-shop\",\"icon\": \"s-shop\"}');
INSERT INTO `sys_dict` VALUES (64, 'icon', '图标字典', '{\"name\": \"s-marketing\",\"icon\": \"s-marketing\"}');
INSERT INTO `sys_dict` VALUES (65, 'icon', '图标字典', '{\"name\": \"s-flag\",\"icon\": \"s-flag\"}');
INSERT INTO `sys_dict` VALUES (66, 'icon', '图标字典', '{\"name\": \"s-comment\",\"icon\": \"s-comment\"}');
INSERT INTO `sys_dict` VALUES (67, 'icon', '图标字典', '{\"name\": \"s-finance\",\"icon\": \"s-finance\"}');
INSERT INTO `sys_dict` VALUES (68, 'icon', '图标字典', '{\"name\": \"s-claim\",\"icon\": \"s-claim\"}');
INSERT INTO `sys_dict` VALUES (69, 'icon', '图标字典', '{\"name\": \"s-custom\",\"icon\": \"s-custom\"}');
INSERT INTO `sys_dict` VALUES (70, 'icon', '图标字典', '{\"name\": \"s-opportunity\",\"icon\": \"s-opportunity\"}');
INSERT INTO `sys_dict` VALUES (71, 'icon', '图标字典', '{\"name\": \"s-data\",\"icon\": \"s-data\"}');
INSERT INTO `sys_dict` VALUES (72, 'icon', '图标字典', '{\"name\": \"s-check\",\"icon\": \"s-check\"}');
INSERT INTO `sys_dict` VALUES (73, 'icon', '图标字典', '{\"name\": \"s-grid\",\"icon\": \"s-grid\"}');
INSERT INTO `sys_dict` VALUES (74, 'icon', '图标字典', '{\"name\": \"menu\",\"icon\": \"menu\"}');
INSERT INTO `sys_dict` VALUES (75, 'icon', '图标字典', '{\"name\": \"share\",\"icon\": \"share\"}');
INSERT INTO `sys_dict` VALUES (76, 'icon', '图标字典', '{\"name\": \"d-caret\",\"icon\": \"d-caret\"}');
INSERT INTO `sys_dict` VALUES (77, 'icon', '图标字典', '{\"name\": \"caret-left\",\"icon\": \"caret-left\"}');
INSERT INTO `sys_dict` VALUES (78, 'icon', '图标字典', '{\"name\": \"caret-right\",\"icon\": \"caret-right\"}');
INSERT INTO `sys_dict` VALUES (79, 'icon', '图标字典', '{\"name\": \"caret-bottom\",\"icon\": \"caret-bottom\"}');
INSERT INTO `sys_dict` VALUES (80, 'icon', '图标字典', '{\"name\": \"caret-top\",\"icon\": \"caret-top\"}');
INSERT INTO `sys_dict` VALUES (81, 'icon', '图标字典', '{\"name\": \"bottom-left\",\"icon\": \"bottom-left\"}');
INSERT INTO `sys_dict` VALUES (82, 'icon', '图标字典', '{\"name\": \"bottom-right\",\"icon\": \"bottom-right\"}');
INSERT INTO `sys_dict` VALUES (83, 'icon', '图标字典', '{\"name\": \"back\",\"icon\": \"back\"}');
INSERT INTO `sys_dict` VALUES (84, 'icon', '图标字典', '{\"name\": \"right\",\"icon\": \"right\"}');
INSERT INTO `sys_dict` VALUES (85, 'icon', '图标字典', '{\"name\": \"bottom\",\"icon\": \"bottom\"}');
INSERT INTO `sys_dict` VALUES (86, 'icon', '图标字典', '{\"name\": \"top\",\"icon\": \"top\"}');
INSERT INTO `sys_dict` VALUES (87, 'icon', '图标字典', '{\"name\": \"top-left\",\"icon\": \"top-left\"}');
INSERT INTO `sys_dict` VALUES (88, 'icon', '图标字典', '{\"name\": \"top-right\",\"icon\": \"top-right\"}');
INSERT INTO `sys_dict` VALUES (89, 'icon', '图标字典', '{\"name\": \"arrow-left\",\"icon\": \"arrow-left\"}');
INSERT INTO `sys_dict` VALUES (90, 'icon', '图标字典', '{\"name\": \"arrow-right\",\"icon\": \"arrow-right\"}');
INSERT INTO `sys_dict` VALUES (91, 'icon', '图标字典', '{\"name\": \"arrow-down\",\"icon\": \"arrow-down\"}');
INSERT INTO `sys_dict` VALUES (92, 'icon', '图标字典', '{\"name\": \"arrow-up\",\"icon\": \"arrow-up\"}');
INSERT INTO `sys_dict` VALUES (93, 'icon', '图标字典', '{\"name\": \"d-arrow-left\",\"icon\": \"d-arrow-left\"}');
INSERT INTO `sys_dict` VALUES (94, 'icon', '图标字典', '{\"name\": \"d-arrow-right\",\"icon\": \"d-arrow-right\"}');
INSERT INTO `sys_dict` VALUES (95, 'icon', '图标字典', '{\"name\": \"video-pause\",\"icon\": \"video-pause\"}');
INSERT INTO `sys_dict` VALUES (96, 'icon', '图标字典', '{\"name\": \"video-play\",\"icon\": \"video-play\"}');
INSERT INTO `sys_dict` VALUES (97, 'icon', '图标字典', '{\"name\": \"refresh\",\"icon\": \"refresh\"}');
INSERT INTO `sys_dict` VALUES (98, 'icon', '图标字典', '{\"name\": \"refresh-right\",\"icon\": \"refresh-right\"}');
INSERT INTO `sys_dict` VALUES (99, 'icon', '图标字典', '{\"name\": \"refresh-left\",\"icon\": \"refresh-left\"}');
INSERT INTO `sys_dict` VALUES (100, 'icon', '图标字典', '{\"name\": \"finished\",\"icon\": \"finished\"}');
INSERT INTO `sys_dict` VALUES (101, 'icon', '图标字典', '{\"name\": \"sort\",\"icon\": \"sort\"}');
INSERT INTO `sys_dict` VALUES (102, 'icon', '图标字典', '{\"name\": \"sort-up\",\"icon\": \"sort-up\"}');
INSERT INTO `sys_dict` VALUES (103, 'icon', '图标字典', '{\"name\": \"sort-down\",\"icon\": \"sort-down\"}');
INSERT INTO `sys_dict` VALUES (104, 'icon', '图标字典', '{\"name\": \"rank\",\"icon\": \"rank\"}');
INSERT INTO `sys_dict` VALUES (105, 'icon', '图标字典', '{\"name\": \"loading\",\"icon\": \"loading\"}');
INSERT INTO `sys_dict` VALUES (106, 'icon', '图标字典', '{\"name\": \"view\",\"icon\": \"view\"}');
INSERT INTO `sys_dict` VALUES (107, 'icon', '图标字典', '{\"name\": \"c-scale-to-original\",\"icon\": \"c-scale-to-original\"}');
INSERT INTO `sys_dict` VALUES (108, 'icon', '图标字典', '{\"name\": \"date\",\"icon\": \"date\"}');
INSERT INTO `sys_dict` VALUES (109, 'icon', '图标字典', '{\"name\": \"edit\",\"icon\": \"edit\"}');
INSERT INTO `sys_dict` VALUES (110, 'icon', '图标字典', '{\"name\": \"edit-outline\",\"icon\": \"edit-outline\"}');
INSERT INTO `sys_dict` VALUES (111, 'icon', '图标字典', '{\"name\": \"folder\",\"icon\": \"folder\"}');
INSERT INTO `sys_dict` VALUES (112, 'icon', '图标字典', '{\"name\": \"folder-opened\",\"icon\": \"folder-opened\"}');
INSERT INTO `sys_dict` VALUES (113, 'icon', '图标字典', '{\"name\": \"folder-add\",\"icon\": \"folder-add\"}');
INSERT INTO `sys_dict` VALUES (114, 'icon', '图标字典', '{\"name\": \"folder-remove\",\"icon\": \"folder-remove\"}');
INSERT INTO `sys_dict` VALUES (115, 'icon', '图标字典', '{\"name\": \"folder-delete\",\"icon\": \"folder-delete\"}');
INSERT INTO `sys_dict` VALUES (116, 'icon', '图标字典', '{\"name\": \"folder-checked\",\"icon\": \"folder-checked\"}');
INSERT INTO `sys_dict` VALUES (117, 'icon', '图标字典', '{\"name\": \"tickets\",\"icon\": \"tickets\"}');
INSERT INTO `sys_dict` VALUES (118, 'icon', '图标字典', '{\"name\": \"document-remove\",\"icon\": \"document-remove\"}');
INSERT INTO `sys_dict` VALUES (119, 'icon', '图标字典', '{\"name\": \"document-delete\",\"icon\": \"document-delete\"}');
INSERT INTO `sys_dict` VALUES (120, 'icon', '图标字典', '{\"name\": \"document-copy\",\"icon\": \"document-copy\"}');
INSERT INTO `sys_dict` VALUES (121, 'icon', '图标字典', '{\"name\": \"document-checked\",\"icon\": \"document-checked\"}');
INSERT INTO `sys_dict` VALUES (122, 'icon', '图标字典', '{\"name\": \"document\",\"icon\": \"document\"}');
INSERT INTO `sys_dict` VALUES (123, 'icon', '图标字典', '{\"name\": \"document-add\",\"icon\": \"document-add\"}');
INSERT INTO `sys_dict` VALUES (124, 'icon', '图标字典', '{\"name\": \"printer\",\"icon\": \"printer\"}');
INSERT INTO `sys_dict` VALUES (125, 'icon', '图标字典', '{\"name\": \"paperclip\",\"icon\": \"paperclip\"}');
INSERT INTO `sys_dict` VALUES (126, 'icon', '图标字典', '{\"name\": \"takeaway-box\",\"icon\": \"takeaway-box\"}');
INSERT INTO `sys_dict` VALUES (127, 'icon', '图标字典', '{\"name\": \"search\",\"icon\": \"search\"}');
INSERT INTO `sys_dict` VALUES (128, 'icon', '图标字典', '{\"name\": \"monitor\",\"icon\": \"monitor\"}');
INSERT INTO `sys_dict` VALUES (129, 'icon', '图标字典', '{\"name\": \"attract\",\"icon\": \"attract\"}');
INSERT INTO `sys_dict` VALUES (130, 'icon', '图标字典', '{\"name\": \"mobile\",\"icon\": \"mobile\"}');
INSERT INTO `sys_dict` VALUES (131, 'icon', '图标字典', '{\"name\": \"scissors\",\"icon\": \"scissors\"}');
INSERT INTO `sys_dict` VALUES (132, 'icon', '图标字典', '{\"name\": \"umbrella\",\"icon\": \"umbrella\"}');
INSERT INTO `sys_dict` VALUES (133, 'icon', '图标字典', '{\"name\": \"headset\",\"icon\": \"headset\"}');
INSERT INTO `sys_dict` VALUES (134, 'icon', '图标字典', '{\"name\": \"brush\",\"icon\": \"brush\"}');
INSERT INTO `sys_dict` VALUES (135, 'icon', '图标字典', '{\"name\": \"mouse\",\"icon\": \"mouse\"}');
INSERT INTO `sys_dict` VALUES (136, 'icon', '图标字典', '{\"name\": \"coordinate\",\"icon\": \"coordinate\"}');
INSERT INTO `sys_dict` VALUES (137, 'icon', '图标字典', '{\"name\": \"magic-stick\",\"icon\": \"magic-stick\"}');
INSERT INTO `sys_dict` VALUES (138, 'icon', '图标字典', '{\"name\": \"reading\",\"icon\": \"reading\"}');
INSERT INTO `sys_dict` VALUES (139, 'icon', '图标字典', '{\"name\": \"data-line\",\"icon\": \"data-line\"}');
INSERT INTO `sys_dict` VALUES (140, 'icon', '图标字典', '{\"name\": \"data-board\",\"icon\": \"data-board\"}');
INSERT INTO `sys_dict` VALUES (141, 'icon', '图标字典', '{\"name\": \"pie-chart\",\"icon\": \"pie-chart\"}');
INSERT INTO `sys_dict` VALUES (142, 'icon', '图标字典', '{\"name\": \"data-analysis\",\"icon\": \"data-analysis\"}');
INSERT INTO `sys_dict` VALUES (143, 'icon', '图标字典', '{\"name\": \"collection-tag\",\"icon\": \"collection-tag\"}');
INSERT INTO `sys_dict` VALUES (144, 'icon', '图标字典', '{\"name\": \"film\",\"icon\": \"film\"}');
INSERT INTO `sys_dict` VALUES (145, 'icon', '图标字典', '{\"name\": \"suitcase\",\"icon\": \"suitcase\"}');
INSERT INTO `sys_dict` VALUES (146, 'icon', '图标字典', '{\"name\": \"suitcase-1\",\"icon\": \"suitcase-1\"}');
INSERT INTO `sys_dict` VALUES (147, 'icon', '图标字典', '{\"name\": \"receiving\",\"icon\": \"receiving\"}');
INSERT INTO `sys_dict` VALUES (148, 'icon', '图标字典', '{\"name\": \"collection\",\"icon\": \"collection\"}');
INSERT INTO `sys_dict` VALUES (149, 'icon', '图标字典', '{\"name\": \"files\",\"icon\": \"files\"}');
INSERT INTO `sys_dict` VALUES (150, 'icon', '图标字典', '{\"name\": \"notebook-1\",\"icon\": \"notebook-1\"}');
INSERT INTO `sys_dict` VALUES (151, 'icon', '图标字典', '{\"name\": \"notebook-2\",\"icon\": \"notebook-2\"}');
INSERT INTO `sys_dict` VALUES (152, 'icon', '图标字典', '{\"name\": \"toilet-paper\",\"icon\": \"toilet-paper\"}');
INSERT INTO `sys_dict` VALUES (153, 'icon', '图标字典', '{\"name\": \"office-building\",\"icon\": \"office-building\"}');
INSERT INTO `sys_dict` VALUES (154, 'icon', '图标字典', '{\"name\": \"school\",\"icon\": \"school\"}');
INSERT INTO `sys_dict` VALUES (155, 'icon', '图标字典', '{\"name\": \"table-lamp\",\"icon\": \"table-lamp\"}');
INSERT INTO `sys_dict` VALUES (156, 'icon', '图标字典', '{\"name\": \"house\",\"icon\": \"house\"}');
INSERT INTO `sys_dict` VALUES (157, 'icon', '图标字典', '{\"name\": \"no-smoking\",\"icon\": \"no-smoking\"}');
INSERT INTO `sys_dict` VALUES (158, 'icon', '图标字典', '{\"name\": \"smoking\",\"icon\": \"smoking\"}');
INSERT INTO `sys_dict` VALUES (159, 'icon', '图标字典', '{\"name\": \"shopping-cart-full\",\"icon\": \"shopping-cart-full\"}');
INSERT INTO `sys_dict` VALUES (160, 'icon', '图标字典', '{\"name\": \"shopping-cart-1\",\"icon\": \"shopping-cart-1\"}');
INSERT INTO `sys_dict` VALUES (161, 'icon', '图标字典', '{\"name\": \"shopping-cart-2\",\"icon\": \"shopping-cart-2\"}');
INSERT INTO `sys_dict` VALUES (162, 'icon', '图标字典', '{\"name\": \"shopping-bag-1\",\"icon\": \"shopping-bag-1\"}');
INSERT INTO `sys_dict` VALUES (163, 'icon', '图标字典', '{\"name\": \"shopping-bag-2\",\"icon\": \"shopping-bag-2\"}');
INSERT INTO `sys_dict` VALUES (164, 'icon', '图标字典', '{\"name\": \"sold-out\",\"icon\": \"sold-out\"}');
INSERT INTO `sys_dict` VALUES (165, 'icon', '图标字典', '{\"name\": \"sell\",\"icon\": \"sell\"}');
INSERT INTO `sys_dict` VALUES (166, 'icon', '图标字典', '{\"name\": \"present\",\"icon\": \"present\"}');
INSERT INTO `sys_dict` VALUES (167, 'icon', '图标字典', '{\"name\": \"box\",\"icon\": \"box\"}');
INSERT INTO `sys_dict` VALUES (168, 'icon', '图标字典', '{\"name\": \"bank-card\",\"icon\": \"bank-card\"}');
INSERT INTO `sys_dict` VALUES (169, 'icon', '图标字典', '{\"name\": \"money\",\"icon\": \"money\"}');
INSERT INTO `sys_dict` VALUES (170, 'icon', '图标字典', '{\"name\": \"coin\",\"icon\": \"coin\"}');
INSERT INTO `sys_dict` VALUES (171, 'icon', '图标字典', '{\"name\": \"wallet\",\"icon\": \"wallet\"}');
INSERT INTO `sys_dict` VALUES (172, 'icon', '图标字典', '{\"name\": \"discount\",\"icon\": \"discount\"}');
INSERT INTO `sys_dict` VALUES (173, 'icon', '图标字典', '{\"name\": \"price-tag\",\"icon\": \"price-tag\"}');
INSERT INTO `sys_dict` VALUES (174, 'icon', '图标字典', '{\"name\": \"news\",\"icon\": \"news\"}');
INSERT INTO `sys_dict` VALUES (175, 'icon', '图标字典', '{\"name\": \"guide\",\"icon\": \"guide\"}');
INSERT INTO `sys_dict` VALUES (176, 'icon', '图标字典', '{\"name\": \"male\",\"icon\": \"male\"}');
INSERT INTO `sys_dict` VALUES (177, 'icon', '图标字典', '{\"name\": \"female\",\"icon\": \"female\"}');
INSERT INTO `sys_dict` VALUES (178, 'icon', '图标字典', '{\"name\": \"thumb\",\"icon\": \"thumb\"}');
INSERT INTO `sys_dict` VALUES (179, 'icon', '图标字典', '{\"name\": \"cpu\",\"icon\": \"cpu\"}');
INSERT INTO `sys_dict` VALUES (180, 'icon', '图标字典', '{\"name\": \"link\",\"icon\": \"link\"}');
INSERT INTO `sys_dict` VALUES (181, 'icon', '图标字典', '{\"name\": \"connection\",\"icon\": \"connection\"}');
INSERT INTO `sys_dict` VALUES (182, 'icon', '图标字典', '{\"name\": \"open\",\"icon\": \"open\"}');
INSERT INTO `sys_dict` VALUES (183, 'icon', '图标字典', '{\"name\": \"turn-off\",\"icon\": \"turn-off\"}');
INSERT INTO `sys_dict` VALUES (184, 'icon', '图标字典', '{\"name\": \"set-up\",\"icon\": \"set-up\"}');
INSERT INTO `sys_dict` VALUES (185, 'icon', '图标字典', '{\"name\": \"chat-round\",\"icon\": \"chat-round\"}');
INSERT INTO `sys_dict` VALUES (186, 'icon', '图标字典', '{\"name\": \"chat-line-round\",\"icon\": \"chat-line-round\"}');
INSERT INTO `sys_dict` VALUES (187, 'icon', '图标字典', '{\"name\": \"chat-square\",\"icon\": \"chat-square\"}');
INSERT INTO `sys_dict` VALUES (188, 'icon', '图标字典', '{\"name\": \"chat-dot-round\",\"icon\": \"chat-dot-round\"}');
INSERT INTO `sys_dict` VALUES (189, 'icon', '图标字典', '{\"name\": \"chat-dot-square\",\"icon\": \"chat-dot-square\"}');
INSERT INTO `sys_dict` VALUES (190, 'icon', '图标字典', '{\"name\": \"chat-line-square\",\"icon\": \"chat-line-square\"}');
INSERT INTO `sys_dict` VALUES (191, 'icon', '图标字典', '{\"name\": \"message\",\"icon\": \"message\"}');
INSERT INTO `sys_dict` VALUES (192, 'icon', '图标字典', '{\"name\": \"postcard\",\"icon\": \"postcard\"}');
INSERT INTO `sys_dict` VALUES (193, 'icon', '图标字典', '{\"name\": \"position\",\"icon\": \"position\"}');
INSERT INTO `sys_dict` VALUES (194, 'icon', '图标字典', '{\"name\": \"turn-off-microphone\",\"icon\": \"turn-off-microphone\"}');
INSERT INTO `sys_dict` VALUES (195, 'icon', '图标字典', '{\"name\": \"microphone\",\"icon\": \"microphone\"}');
INSERT INTO `sys_dict` VALUES (196, 'icon', '图标字典', '{\"name\": \"close-notification\",\"icon\": \"close-notification\"}');
INSERT INTO `sys_dict` VALUES (197, 'icon', '图标字典', '{\"name\": \"bangzhu\",\"icon\": \"bangzhu\"}');
INSERT INTO `sys_dict` VALUES (198, 'icon', '图标字典', '{\"name\": \"time\",\"icon\": \"time\"}');
INSERT INTO `sys_dict` VALUES (199, 'icon', '图标字典', '{\"name\": \"odometer\",\"icon\": \"odometer\"}');
INSERT INTO `sys_dict` VALUES (200, 'icon', '图标字典', '{\"name\": \"crop\",\"icon\": \"crop\"}');
INSERT INTO `sys_dict` VALUES (201, 'icon', '图标字典', '{\"name\": \"aim\",\"icon\": \"aim\"}');
INSERT INTO `sys_dict` VALUES (202, 'icon', '图标字典', '{\"name\": \"switch-button\",\"icon\": \"switch-button\"}');
INSERT INTO `sys_dict` VALUES (203, 'icon', '图标字典', '{\"name\": \"full-screen\",\"icon\": \"full-screen\"}');
INSERT INTO `sys_dict` VALUES (204, 'icon', '图标字典', '{\"name\": \"copy-document\",\"icon\": \"copy-document\"}');
INSERT INTO `sys_dict` VALUES (205, 'icon', '图标字典', '{\"name\": \"mic\",\"icon\": \"mic\"}');
INSERT INTO `sys_dict` VALUES (206, 'icon', '图标字典', '{\"name\": \"stopwatch\",\"icon\": \"stopwatch\"}');
INSERT INTO `sys_dict` VALUES (207, 'icon', '图标字典', '{\"name\": \"medal-1\",\"icon\": \"medal-1\"}');
INSERT INTO `sys_dict` VALUES (208, 'icon', '图标字典', '{\"name\": \"medal\",\"icon\": \"medal\"}');
INSERT INTO `sys_dict` VALUES (209, 'icon', '图标字典', '{\"name\": \"trophy\",\"icon\": \"trophy\"}');
INSERT INTO `sys_dict` VALUES (210, 'icon', '图标字典', '{\"name\": \"trophy-1\",\"icon\": \"trophy-1\"}');
INSERT INTO `sys_dict` VALUES (211, 'icon', '图标字典', '{\"name\": \"first-aid-kit\",\"icon\": \"first-aid-kit\"}');
INSERT INTO `sys_dict` VALUES (212, 'icon', '图标字典', '{\"name\": \"discover\",\"icon\": \"discover\"}');
INSERT INTO `sys_dict` VALUES (213, 'icon', '图标字典', '{\"name\": \"place\",\"icon\": \"place\"}');
INSERT INTO `sys_dict` VALUES (214, 'icon', '图标字典', '{\"name\": \"location\",\"icon\": \"location\"}');
INSERT INTO `sys_dict` VALUES (215, 'icon', '图标字典', '{\"name\": \"location-outline\",\"icon\": \"location-outline\"}');
INSERT INTO `sys_dict` VALUES (216, 'icon', '图标字典', '{\"name\": \"location-information\",\"icon\": \"location-information\"}');
INSERT INTO `sys_dict` VALUES (217, 'icon', '图标字典', '{\"name\": \"add-location\",\"icon\": \"add-location\"}');
INSERT INTO `sys_dict` VALUES (218, 'icon', '图标字典', '{\"name\": \"delete-location\",\"icon\": \"delete-location\"}');
INSERT INTO `sys_dict` VALUES (219, 'icon', '图标字典', '{\"name\": \"map-location\",\"icon\": \"map-location\"}');
INSERT INTO `sys_dict` VALUES (220, 'icon', '图标字典', '{\"name\": \"alarm-clock\",\"icon\": \"alarm-clock\"}');
INSERT INTO `sys_dict` VALUES (221, 'icon', '图标字典', '{\"name\": \"timer\",\"icon\": \"timer\"}');
INSERT INTO `sys_dict` VALUES (222, 'icon', '图标字典', '{\"name\": \"watch-1\",\"icon\": \"watch-1\"}');
INSERT INTO `sys_dict` VALUES (223, 'icon', '图标字典', '{\"name\": \"watch\",\"icon\": \"watch\"}');
INSERT INTO `sys_dict` VALUES (224, 'icon', '图标字典', '{\"name\": \"lock\",\"icon\": \"lock\"}');
INSERT INTO `sys_dict` VALUES (225, 'icon', '图标字典', '{\"name\": \"unlock\",\"icon\": \"unlock\"}');
INSERT INTO `sys_dict` VALUES (226, 'icon', '图标字典', '{\"name\": \"key\",\"icon\": \"key\"}');
INSERT INTO `sys_dict` VALUES (227, 'icon', '图标字典', '{\"name\": \"service\",\"icon\": \"service\"}');
INSERT INTO `sys_dict` VALUES (228, 'icon', '图标字典', '{\"name\": \"mobile-phone\",\"icon\": \"mobile-phone\"}');
INSERT INTO `sys_dict` VALUES (229, 'icon', '图标字典', '{\"name\": \"bicycle\",\"icon\": \"bicycle\"}');
INSERT INTO `sys_dict` VALUES (230, 'icon', '图标字典', '{\"name\": \"truck\",\"icon\": \"truck\"}');
INSERT INTO `sys_dict` VALUES (231, 'icon', '图标字典', '{\"name\": \"ship\",\"icon\": \"ship\"}');
INSERT INTO `sys_dict` VALUES (232, 'icon', '图标字典', '{\"name\": \"basketball\",\"icon\": \"basketball\"}');
INSERT INTO `sys_dict` VALUES (233, 'icon', '图标字典', '{\"name\": \"football\",\"icon\": \"football\"}');
INSERT INTO `sys_dict` VALUES (234, 'icon', '图标字典', '{\"name\": \"soccer\",\"icon\": \"soccer\"}');
INSERT INTO `sys_dict` VALUES (235, 'icon', '图标字典', '{\"name\": \"baseball\",\"icon\": \"baseball\"}');
INSERT INTO `sys_dict` VALUES (236, 'icon', '图标字典', '{\"name\": \"wind-power\",\"icon\": \"wind-power\"}');
INSERT INTO `sys_dict` VALUES (237, 'icon', '图标字典', '{\"name\": \"light-rain\",\"icon\": \"light-rain\"}');
INSERT INTO `sys_dict` VALUES (238, 'icon', '图标字典', '{\"name\": \"lightning\",\"icon\": \"lightning\"}');
INSERT INTO `sys_dict` VALUES (239, 'icon', '图标字典', '{\"name\": \"heavy-rain\",\"icon\": \"heavy-rain\"}');
INSERT INTO `sys_dict` VALUES (240, 'icon', '图标字典', '{\"name\": \"sunrise\",\"icon\": \"sunrise\"}');
INSERT INTO `sys_dict` VALUES (241, 'icon', '图标字典', '{\"name\": \"sunrise-1\",\"icon\": \"sunrise-1\"}');
INSERT INTO `sys_dict` VALUES (242, 'icon', '图标字典', '{\"name\": \"sunset\",\"icon\": \"sunset\"}');
INSERT INTO `sys_dict` VALUES (243, 'icon', '图标字典', '{\"name\": \"sunny\",\"icon\": \"sunny\"}');
INSERT INTO `sys_dict` VALUES (244, 'icon', '图标字典', '{\"name\": \"cloudy\",\"icon\": \"cloudy\"}');
INSERT INTO `sys_dict` VALUES (245, 'icon', '图标字典', '{\"name\": \"partly-cloudy\",\"icon\": \"partly-cloudy\"}');
INSERT INTO `sys_dict` VALUES (246, 'icon', '图标字典', '{\"name\": \"cloudy-and-sunny\",\"icon\": \"cloudy-and-sunny\"}');
INSERT INTO `sys_dict` VALUES (247, 'icon', '图标字典', '{\"name\": \"moon\",\"icon\": \"moon\"}');
INSERT INTO `sys_dict` VALUES (248, 'icon', '图标字典', '{\"name\": \"moon-night\",\"icon\": \"moon-night\"}');
INSERT INTO `sys_dict` VALUES (249, 'icon', '图标字典', '{\"name\": \"dish\",\"icon\": \"dish\"}');
INSERT INTO `sys_dict` VALUES (250, 'icon', '图标字典', '{\"name\": \"dish-1\",\"icon\": \"dish-1\"}');
INSERT INTO `sys_dict` VALUES (251, 'icon', '图标字典', '{\"name\": \"food\",\"icon\": \"food\"}');
INSERT INTO `sys_dict` VALUES (252, 'icon', '图标字典', '{\"name\": \"chicken\",\"icon\": \"chicken\"}');
INSERT INTO `sys_dict` VALUES (253, 'icon', '图标字典', '{\"name\": \"fork-spoon\",\"icon\": \"fork-spoon\"}');
INSERT INTO `sys_dict` VALUES (254, 'icon', '图标字典', '{\"name\": \"knife-fork\",\"icon\": \"knife-fork\"}');
INSERT INTO `sys_dict` VALUES (255, 'icon', '图标字典', '{\"name\": \"burger\",\"icon\": \"burger\"}');
INSERT INTO `sys_dict` VALUES (256, 'icon', '图标字典', '{\"name\": \"tableware\",\"icon\": \"tableware\"}');
INSERT INTO `sys_dict` VALUES (257, 'icon', '图标字典', '{\"name\": \"sugar\",\"icon\": \"sugar\"}');
INSERT INTO `sys_dict` VALUES (258, 'icon', '图标字典', '{\"name\": \"dessert\",\"icon\": \"dessert\"}');
INSERT INTO `sys_dict` VALUES (259, 'icon', '图标字典', '{\"name\": \"ice-cream\",\"icon\": \"ice-cream\"}');
INSERT INTO `sys_dict` VALUES (260, 'icon', '图标字典', '{\"name\": \"hot-water\",\"icon\": \"hot-water\"}');
INSERT INTO `sys_dict` VALUES (261, 'icon', '图标字典', '{\"name\": \"water-cup\",\"icon\": \"water-cup\"}');
INSERT INTO `sys_dict` VALUES (262, 'icon', '图标字典', '{\"name\": \"coffee-cup\",\"icon\": \"coffee-cup\"}');
INSERT INTO `sys_dict` VALUES (263, 'icon', '图标字典', '{\"name\": \"cold-drink\",\"icon\": \"cold-drink\"}');
INSERT INTO `sys_dict` VALUES (264, 'icon', '图标字典', '{\"name\": \"goblet\",\"icon\": \"goblet\"}');
INSERT INTO `sys_dict` VALUES (265, 'icon', '图标字典', '{\"name\": \"goblet-full\",\"icon\": \"goblet-full\"}');
INSERT INTO `sys_dict` VALUES (266, 'icon', '图标字典', '{\"name\": \"goblet-square\",\"icon\": \"goblet-square\"}');
INSERT INTO `sys_dict` VALUES (267, 'icon', '图标字典', '{\"name\": \"goblet-square-full\",\"icon\": \"goblet-square-full\"}');
INSERT INTO `sys_dict` VALUES (268, 'icon', '图标字典', '{\"name\": \"refrigerator\",\"icon\": \"refrigerator\"}');
INSERT INTO `sys_dict` VALUES (269, 'icon', '图标字典', '{\"name\": \"grape\",\"icon\": \"grape\"}');
INSERT INTO `sys_dict` VALUES (270, 'icon', '图标字典', '{\"name\": \"watermelon\",\"icon\": \"watermelon\"}');
INSERT INTO `sys_dict` VALUES (271, 'icon', '图标字典', '{\"name\": \"cherry\",\"icon\": \"cherry\"}');
INSERT INTO `sys_dict` VALUES (272, 'icon', '图标字典', '{\"name\": \"apple\",\"icon\": \"apple\"}');
INSERT INTO `sys_dict` VALUES (273, 'icon', '图标字典', '{\"name\": \"pear\",\"icon\": \"pear\"}');
INSERT INTO `sys_dict` VALUES (274, 'icon', '图标字典', '{\"name\": \"orange\",\"icon\": \"orange\"}');
INSERT INTO `sys_dict` VALUES (275, 'icon', '图标字典', '{\"name\": \"coffee\",\"icon\": \"coffee\"}');
INSERT INTO `sys_dict` VALUES (276, 'icon', '图标字典', '{\"name\": \"ice-tea\",\"icon\": \"ice-tea\"}');
INSERT INTO `sys_dict` VALUES (277, 'icon', '图标字典', '{\"name\": \"ice-drink\",\"icon\": \"ice-drink\"}');
INSERT INTO `sys_dict` VALUES (278, 'icon', '图标字典', '{\"name\": \"milk-tea\",\"icon\": \"milk-tea\"}');
INSERT INTO `sys_dict` VALUES (279, 'icon', '图标字典', '{\"name\": \"potato-strips\",\"icon\": \"potato-strips\"}');
INSERT INTO `sys_dict` VALUES (280, 'icon', '图标字典', '{\"name\": \"lollipop\",\"icon\": \"lollipop\"}');
INSERT INTO `sys_dict` VALUES (281, 'icon', '图标字典', '{\"name\": \"ice-cream-square\",\"icon\": \"ice-cream-square\"}');
INSERT INTO `sys_dict` VALUES (282, 'icon', '图标字典', '{\"name\": \"ice-cream-round\",\"icon\": \"ice-cream-round\"}');
INSERT INTO `sys_dict` VALUES (283, 'icon', '图标字典', '{\"name\": \"platform-eleme\",\"icon\": \"platform-eleme\"}');
INSERT INTO `sys_dict` VALUES (284, 'menuType', '菜单字典', '{\"name\": \"菜单\",type: 1}');
INSERT INTO `sys_dict` VALUES (285, 'menuType', '菜单字典', '{\"name\": \"按钮\",type: 2}');
INSERT INTO `sys_dict` VALUES (286, 'menuType', '菜单字典', '{\"name\": \"目录\",type: 0}');
INSERT INTO `sys_dict` VALUES (287, 'menuType', '菜单字典', '{\"name\": \"无类型\",type: 3}');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
  `stream` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件数据流',
  `project_id` int NULL DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_file_ibfk_1`(`project_id` ASC) USING BTREE,
  CONSTRAINT `sys_file_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '附件表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_friend
-- ----------------------------
DROP TABLE IF EXISTS `sys_friend`;
CREATE TABLE `sys_friend`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id_a` int NULL DEFAULT NULL COMMENT '用户A的id',
  `user_id_b` int NULL DEFAULT NULL COMMENT '用户B的id',
  `user_a` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户A的账号',
  `user_b` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户B的账号',
  `status` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT 'A向B发起好友申请，流程状态（1.默认，2.同意，3.拒绝）',
  `notes` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'A对B的备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `useridA`(`user_id_a` ASC) USING BTREE,
  INDEX `useridB`(`user_id_b` ASC) USING BTREE,
  INDEX `好友表A账号外键`(`user_a` ASC) USING BTREE,
  INDEX `好友表B账号外键`(`user_b` ASC) USING BTREE,
  CONSTRAINT `sys_friend_ibfk_1` FOREIGN KEY (`user_id_a`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_friend_ibfk_2` FOREIGN KEY (`user_id_b`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_friend_ibfk_3` FOREIGN KEY (`user_a`) REFERENCES `sys_user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_friend_ibfk_4` FOREIGN KEY (`user_b`) REFERENCES `sys_user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '好友关系表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_image
-- ----------------------------
DROP TABLE IF EXISTS `sys_image`;
CREATE TABLE `sys_image`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '图片表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_item`;
CREATE TABLE `sys_item`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_by` int NULL DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `project_id` int NULL DEFAULT NULL COMMENT '项目id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `项目`(`project_id` ASC) USING BTREE,
  INDEX `创建者`(`create_by` ASC) USING BTREE,
  CONSTRAINT `sys_item_ibfk_1` FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_item_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目日志表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问接口',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问地址',
  `params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `time` double NULL DEFAULT NULL COMMENT '执行时长(毫秒)',
  `control_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求映射控制类',
  `inner_ip` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求来源',
  `mobile` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备来源',
  `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回结果',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回提示类型',
  `code` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回状态',
  `message` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '返回提示',
  `os` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统',
  `browser` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器信息',
  `method_api` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `class_api` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `账号`(`account` ASC) USING BTREE,
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`account`) REFERENCES `sys_user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_login`;
CREATE TABLE `sys_login`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `time` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录地址',
  `mobile` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问设备',
  `inner_ip` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问来源',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `日志表账号外键`(`account` ASC) USING BTREE,
  CONSTRAINT `sys_login_ibfk_1` FOREIGN KEY (`account`) REFERENCES `sys_user` (`account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录日志表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `type` int NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int NULL DEFAULT NULL COMMENT '排序',
  `access` int NULL DEFAULT NULL COMMENT '显示此菜单需要的角色权限',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组成',
  `com_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  `is_show` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否显示',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '首页', '/Index', 0, 's-home', 1, 11, '/Index', 'Index', '显示');
INSERT INTO `sys_menu` VALUES (2, 0, '系统管理', '/sys', 0, 'setting', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (3, 0, '系统工具', '/tool', 0, 'coordinate', 3, 1, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (4, 0, '用户管理', '/user', 0, 's-custom', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (5, 0, '项目管理', '/project', 0, 's-order', 5, 11, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (6, 0, '数据管理', '/count', 0, 'notebook-2', 6, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (7, 0, '好友管理', '/friend', 0, 's-unfold', 7, 11, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (8, 0, '个人中心', '/personal', 0, 's-custom', 8, 11, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (9, 2, '菜单管理', '/SysMenu', 1, 'setting', 1, 1, '/sys/SysMenu', 'SysMenu', '显示');
INSERT INTO `sys_menu` VALUES (10, 2, '接口管理', '/SysInterface', 1, 'money', 2, 1, '/sys/SysInterface', 'SysInterface', '显示');
INSERT INTO `sys_menu` VALUES (11, 2, '角色管理', '/SysRole', 1, 'setting', 3, 2, '/sys/SysRole', 'SysRole', '显示');
INSERT INTO `sys_menu` VALUES (12, 2, '项目管理', '/SysProject', 1, 's-order', 4, 2, '/sys/SysProject', 'SysProject', '显示');
INSERT INTO `sys_menu` VALUES (13, 2, '用户管理', '/SysUser', 1, 'user', 5, 1, '/sys/SysUser', 'SysUser', '显示');
INSERT INTO `sys_menu` VALUES (14, 2, '在线用户', '/SysUserOnline', 1, 'loading', 6, 2, '/sys/SysUserOnline', 'SysUserOnline', '显示');
INSERT INTO `sys_menu` VALUES (15, 2, '虚拟登录', '/SysVirtualLogin', 1, 'warning', 7, 1, '/sys/SysVirtualLogin', 'SysVirtualLogin', '显示');
INSERT INTO `sys_menu` VALUES (16, 2, '实体管理', '/SysDomain', 1, 'notebook-2', 8, 2, '/sys/SysDomain', 'SysDomain', '显示');
INSERT INTO `sys_menu` VALUES (17, 2, '系统公告', '/SysNotices', 1, 'office-building', 9, 2, '/sys/SysNotices', 'SysNotices', '显示');
INSERT INTO `sys_menu` VALUES (18, 2, '登录记录', '/SysLogin', 1, 's-data', 10, 2, '/sys/SysLogin', 'SysLogin', '显示');
INSERT INTO `sys_menu` VALUES (19, 2, '日志管理', '/SysLog', 1, 'setting', 11, 2, '/sys/SysLog', 'SysLog', '显示');
INSERT INTO `sys_menu` VALUES (20, 2, '参数管理', '/SysConfig', 1, 'edit', 12, 2, '/sys/SysConfig', 'SysConfig', '显示');
INSERT INTO `sys_menu` VALUES (21, 3, '前端代码生成', '/Vue', 1, 's-unfold', 1, 1, '/tool/Vue', 'Vue', '显示');
INSERT INTO `sys_menu` VALUES (22, 3, '后端代码生成', '/Java', 1, 'eleme', 2, 1, '/tool/Java', 'Java', '显示');
INSERT INTO `sys_menu` VALUES (23, 3, '接口文档', '/Swagger', 1, 'thumb', 3, 11, '/tool/Swagger', 'Swagger', '显示');
INSERT INTO `sys_menu` VALUES (24, 4, '用户列表', '/UserList', 1, 's-custom', 1, 2, '/user/UserList', 'UserList', '显示');
INSERT INTO `sys_menu` VALUES (25, 5, '项目列表', '/ProjectList', 1, 's-order', 1, 11, '/project/ProjectList', 'ProjectList', '显示');
INSERT INTO `sys_menu` VALUES (26, 5, '我的项目', '/MyProject', 1, 's-order', 2, 11, '/project/MyProject', 'MyProject', '显示');
INSERT INTO `sys_menu` VALUES (27, 5, '审核项目', '/ProcessProject', 1, 'c-scale-to-original', 3, 2, '/project/ProcessProject', 'ProcessProject', '显示');
INSERT INTO `sys_menu` VALUES (28, 5, '分配项目', '/ShareProject', 1, 'tickets\r\ne', 4, 1, '/project/ShareProject', 'ShareProject', '显示');
INSERT INTO `sys_menu` VALUES (29, 6, '项目统计', '/ItemCount', 1, 'more-outline', 1, 1, '/count/ItemCount', 'ItemCount', '显示');
INSERT INTO `sys_menu` VALUES (30, 6, '数据统计', '/DataCount', 1, 'document-copy', 3, 1, '/count/DataCount', 'DataCount', '显示');
INSERT INTO `sys_menu` VALUES (31, 6, '个人项目统计', '/PersonItem', 1, 's-data', 4, 11, '/count/PersonItem', 'PersonItem', '显示');
INSERT INTO `sys_menu` VALUES (32, 7, '好友列表', '/FriendList', 1, 's-grid', 1, 11, '/friend/FriendList', 'FriendList', '显示');
INSERT INTO `sys_menu` VALUES (33, 7, '申请列表', '/ApplicationList', 1, 'message-solid', 2, 11, '/friend/ApplicationList', 'ApplicationList', '显示');
INSERT INTO `sys_menu` VALUES (34, 7, '申请记录', '/ApplicationLog', 1, 'bell', 3, 11, '/friend/ApplicationLog', 'ApplicationLog', '显示');
INSERT INTO `sys_menu` VALUES (35, 8, '个人资料', '/PersonalData', 1, 's-management', 1, 11, '/personal/PersonalData', 'PersonalData', '显示');
INSERT INTO `sys_menu` VALUES (36, 9, '打印', '/print', 2, 'printer', 1, 1, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (37, 9, '新增', '/insert', 2, 'plus', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (38, 9, '修改', '/update', 2, 'edit', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (39, 9, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (40, 10, '打印', '/print', 2, 'printer', 1, 1, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (41, 11, '打印', '/print', 2, 'printer', 1, 1, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (42, 11, '新增', '/insert', 2, 'plus', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (43, 11, '修改', '/update', 2, 'edit', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (44, 11, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (45, 12, '打印', '/print', 2, 'printer', 1, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (46, 12, '新增', '/insert', 2, 'plus', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (47, 12, '修改', '/update', 2, 'edit', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (48, 12, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (49, 13, '打印', '/print', 2, 'printer', 1, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (50, 13, '新增', '/insert', 2, 'plus', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (51, 13, '修改', '/update', 2, 'edit', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (52, 13, '导入', '/import', 2, 'upload2', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (53, 13, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (54, 13, '导出', '/export', 2, 'download', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (55, 17, '打印', '/print', 2, 'printer', 1, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (56, 17, '新增', '/insert', 2, 'plus', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (57, 17, '修改', '/update', 2, 'edit', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (58, 17, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (59, 19, '打印', '/print', 2, 'printer', 1, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (60, 19, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (61, 20, '打印', '/print', 2, 'printer', 1, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (62, 20, '新增', '/insert', 2, 'plus', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (63, 20, '修改', '/update', 2, 'edit', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (64, 20, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (65, 24, '打印', '/print', 2, 'printer', 1, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (66, 24, '新增', '/insert', 2, 'plus', 2, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (67, 24, '修改', '/update', 2, 'edit', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (68, 24, '导入', '/import', 2, 'upload2', 3, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (69, 24, '删除', '/delete', 2, 'delete', 4, 2, NULL, NULL, '显示');
INSERT INTO `sys_menu` VALUES (70, 24, '导出', '/export', 2, 'download', 4, 2, NULL, NULL, '显示');

-- ----------------------------
-- Table structure for sys_notices
-- ----------------------------
DROP TABLE IF EXISTS `sys_notices`;
CREATE TABLE `sys_notices`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告内容',
  `time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布时间',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `notice_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统公告表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_perm`;
CREATE TABLE `sys_perm`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `perm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `jsjb` int NOT NULL COMMENT '需要的角色级别',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `perm`(`perm` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for sys_process
-- ----------------------------
DROP TABLE IF EXISTS `sys_process`;
CREATE TABLE `sys_process`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `project_id` int NULL DEFAULT NULL COMMENT '项目id',
  `user_id` int NULL DEFAULT NULL COMMENT '审核人员id',
  `process` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核结果',
  `remark` varchar(21) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  `is_process` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否审核过(YES NO)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `创建人`(`user_id` ASC) USING BTREE,
  INDEX `项目id`(`project_id` ASC) USING BTREE,
  CONSTRAINT `sys_process_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_process_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目审核表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_project
-- ----------------------------
DROP TABLE IF EXISTS `sys_project`;
CREATE TABLE `sys_project`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目内容',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '科研类型',
  `money` float NULL DEFAULT NULL COMMENT '申请资金',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目内容表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT ' 主键 ',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 角色名称 ',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 备注 ',
  `create_by` int NULL DEFAULT NULL COMMENT ' 创建者Iid',
  `create_time` datetime NULL DEFAULT NULL COMMENT ' 创建时间 ',
  `access` int NULL DEFAULT NULL COMMENT '角色权限',
  `activation` double NULL DEFAULT 0 COMMENT ' 权限需要的活跃度 ',
  `by_act` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否可以通过活跃度获取到此权限',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_by`(`create_by` ASC) USING BTREE,
  CONSTRAINT `sys_role_ibfk_1` FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '系统管理员', '系统管理员（最高权限，什么都可以做，除了改用户密码）', 1, '2025-05-27 13:56:31', 1, 0, 'NO');
INSERT INTO `sys_role` VALUES (2, '学校管理员', '学校', 1, '2025-05-27 13:56:31', 2, 0, 'NO');
INSERT INTO `sys_role` VALUES (3, '学院管理员', '学院', 1, '2025-05-27 13:56:31', 3, 100, 'YES');
INSERT INTO `sys_role` VALUES (4, '科研人员', '用户', 1, '2025-05-27 13:56:31', 4, 60, 'YES');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` int NOT NULL COMMENT '菜单ID',
  `role_id` int NOT NULL COMMENT '角色ID',
  `access` int NOT NULL COMMENT '角色权限',
  `relationship` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对应关系',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id` ASC) USING BTREE,
  INDEX `menu_id`(`menu_id` ASC) USING BTREE,
  CONSTRAINT `sys_role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- Table structure for sys_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_perm`;
CREATE TABLE `sys_role_perm`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NOT NULL COMMENT '角色id',
  `perm_id` int NOT NULL COMMENT '权限id',
  `relationship` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应关系',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id` ASC) USING BTREE,
  INDEX `perm_id`(`perm_id` ASC) USING BTREE,
  CONSTRAINT `sys_role_perm_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_role_perm_ibfk_2` FOREIGN KEY (`perm_id`) REFERENCES `sys_perm` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sys_score
-- ----------------------------
DROP TABLE IF EXISTS `sys_score`;
CREATE TABLE `sys_score`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT ' 主键 ',
  `project_id` int NULL DEFAULT NULL COMMENT '项目id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `score` double(11, 0) NULL DEFAULT NULL COMMENT '评分',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `创建人-评分`(`user_id` ASC) USING BTREE,
  INDEX `项目id-评分`(`project_id` ASC) USING BTREE,
  CONSTRAINT `sys_score_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_score_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `sys_project` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评分表' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for sys_update
-- ----------------------------
DROP TABLE IF EXISTS `sys_update`;
CREATE TABLE `sys_update`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交者',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '更新日志表' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `account` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `image_id` int NULL DEFAULT NULL COMMENT '头像编号',
  `sex` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `userId`(`id` ASC) USING BTREE,
  UNIQUE INDEX `account`(`account` ASC) USING BTREE,
  INDEX `imageId`(`image_id` ASC) USING BTREE,
  CONSTRAINT `sys_user_ibfk_1` FOREIGN KEY (`image_id`) REFERENCES `sys_image` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '1000000000', 'admin', 'afc61b9629fc60baea664072a58a6d273272ecf0963ff36816b653bdaaa55fd9', '3065ba42eb0741a1ad04e088ff4d3611', 'onkayyeo@gmail.com', '13982926784', '正常', '2021-08-05 19:53:09', 'Stanley James', 1, '女', NULL);

-- ----------------------------
-- Table structure for sys_user_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_perm`;
CREATE TABLE `sys_user_perm`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `perm_id` int NOT NULL COMMENT '权限ID',
  `user_id` int NOT NULL COMMENT '账号ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `perm_id`(`perm_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `sys_user_perm_ibfk_1` FOREIGN KEY (`perm_id`) REFERENCES `sys_perm` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_user_perm_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账号权限表' ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `用户权限表用户外键`(`user_id` ASC) USING BTREE,
  INDEX `用户权限表权限外键`(`role_id` ASC) USING BTREE,
  CONSTRAINT `sys_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sys_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (430, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;

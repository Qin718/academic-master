<template>
  <el-card class="box-card">
    <div>

        <right-toolbar :columns="properties" :showSearch.sync="showSearch" @queryTable="getDataList"></right-toolbar>
      </el-row>

      <!-- 数据表单 -->
      <el-table :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
	        v-for="{prop, label, width} in getProp"
	        :key="prop"
	        :label="label"
	        :prop="prop"
	        :width="width"
	        sortable>
          <!-- 图标 -->
          <template v-if="prop==='activation'" v-slot="scope">
            <span>{{ scope.row['byAct'] === "NO" ? "无法获取" : scope.row['activation'] }}</span>
          </template>
        </el-table-column>
	      
      </el-table>
      <!-- 新增按钮打开的页面 -->
      <el-dialog title="新增角色" :visible.sync="insertWindow">
        <el-form :model="insertData">
          <el-form-item label="名称" label-width="120px">
            <el-input
              v-model="insertData.roleName"
              placeholder="名称"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="角色级别" label-width="120px">
            <el-input
              v-model="insertData['access']"
              placeholder="1级最高"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="角色活跃度" label-width="120px">
            <el-input
              v-model="insertData['activation']"
              placeholder="角色紧急所需活跃度"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="描述信息" label-width="120px">
            <el-input
              v-model="insertData['remark']"
              style="width: 80%"
              type="textarea"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="insertWindow = false">取 消</el-button>
          <el-button type="primary" @click="insertRole()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 编辑按钮打开的页面 -->
      <el-dialog title="编辑角色" :visible.sync="editWindow">
        <el-form :model="editData">
          <el-form-item label="角色名称" label-width="120px">
            <el-input
              v-model="editData.roleName"
              placeholder="角色名称"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="角色级别" label-width="120px">
            <el-input
              v-model="editData['access']"
              placeholder="1级最高"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="活跃度" label-width="120px">
            <el-input
              v-model="editData['activation']"
              placeholder="1级最高"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="描述信息" label-width="120px">
            <el-input
              v-model="editData['remark']"
              style="width: 80%"
              type="textarea"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editWindow = false">取 消</el-button>
          <el-button type="primary" @click="updateRole()">更 新</el-button>
        </div>
      </el-dialog>
      <!-- 菜单按钮打开的页面 -->
      <el-dialog title="编辑菜单" :visible.sync="menuWindow">
        <el-tree :check-strictly="true"
                 :data="AllMenu"
                 :default-checked-keys="menu"
                 :props="menuProps"
                 accordion
                 node-key="id"
                 show-checkbox
                 @check="updateMenu"></el-tree>
        <div slot="footer" class="dialog-footer">
          <el-button @click="menuWindow = false">取 消</el-button>
          <el-button type="primary" @click="updateMenu2()">更 新</el-button>
        </div>
      </el-dialog>
      <!-- 权限按钮打开的页面 -->
      <el-dialog title="编辑权限" :visible.sync="permWindow">
        <el-tree :check-strictly="true"
                 :data="AllPerm"
                 :default-checked-keys="perm"
                 :props="permProps"
                 accordion
                 node-key="name"
                 show-checkbox
                 @check="updatePerm"></el-tree>
        <div slot="footer" class="dialog-footer">
          <el-button @click="permWindow = false">取 消</el-button>
          <el-button type="primary" @click="updatePerm2()">更 新</el-button>
        </div>
      </el-dialog>
      
    </div>
  </el-card>
</template>
<script>

import {message} from "@/method/message";

export default {
  name: "SysRole",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      editData: {}, //编辑数据
      insertData: {},//新增数据

      //集合参数
      dataList: [], //页面展示的数据集合
      AllMenu: [],//所有菜单列表
      menu: [],//菜单列表
      menu1: [],//未修改的菜单菜单列表
      AllPerm: [],//所有权限列表
      perm: [],//权限列表
      perm1: [],//未修改的权限列表
      selections: [],//多选数组

      //页号参数
      pageIndex: 1,
      pageSize: 5,
      pageSizes: [],
      totalPage: 0,
      begin: 0,
      end: this.pageSize - 1,

      //窗口显示参数
      insertWindow: false, //默认关闭新建用户界面
      editWindow: false, //默认关闭编辑用户界面
      menuWindow: false, //默认关闭菜单界面
      permWindow: false, //默认关闭权限界面

      //其他
      menuProps: {//菜单列表结构
        children: 'children',
        label: 'name'
      },
      permProps: {//权限列表结构
        label: 'name',
      },
      single: true,//非单个禁用
      multiple: true,//非多个禁用
      roleId: 0,//当前点击的角色级别
      showSearch: true,// 显示搜索条件
      dataListFrom: "getDataList",//当前数据来源于搜索还是全局

      //表头数据
      properties: [],
    };
  },
  methods: {
    //改变数组大小
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.SizeOrCurrentChangeAfter();
    },
    //改变当前页号
    CurrentChangeHandle(val) {
      this.pageIndex = val;
      this.SizeOrCurrentChangeAfter();
    },
    //在更改数组大小或者页号前，判断数据来源
    SizeOrCurrentChangeAfter() {
      if (this.dataListFrom === "getDataList")
        this.getDataList();
      else this.getDataListBySearch();
    },

    //添加角色
    insertRole() {
      const data = {
        roleBo: this.insertData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/role/insert", data).then((res) => {
        this.insertWindow = false;
        this.cutDataList(res);
      });
    },
    //修改角色
    updateRole() {
      const data = {
        roleBo: this.editData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/role/update", data).then((res) => {
        this.cutDataList(res);
        this.editWindow = false;
      });
    },
    //删除角色
    deleteRole(item,data) {
      const list = []
      //判断时多选框中选择，还是单条点击
      if(item === null) {
        list.push(data.id)
      }else {
        this.selections.forEach(item => list.push(item.id))
      }
      this.$confirm("是否确认删除?", "提示", {confirmButtonText: "确定",cancelButtonText: "取消",type: "warning"})
        .then(() => {
          // 表示处理的是 删除操作
          const data = {
            list: list,
            pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
          }
          this.$http.post("/role/delete", data).then((res) => {
            this.cutDataList(res);
          });
        })
        .catch(() => {
          message.info("已取消删除");
        });
    },
    //更改角色菜单
    updateMenu(data) {
      const id = data.id;
      if (this.menu.includes(id)) {
        this.menu = this.menu.filter((menuId) => {//包含就删除
          return menuId !== id;
        });
        if (data.children !== null) {
          data.children.forEach(child => {//删除子菜单所有的id
            this.menu = this.menu.filter((menuId) => {
              return menuId !== child.id;
            });
          })
        }
      } else {//不包含添加
        this.menu.push(id)
        if (data.parentId !== 0) {//添加他的父菜单
          this.menu.push(data.parentId)
        }
      }
      //去重
      this.menu = [...new Set(this.menu)];
    },
    //将更改后的菜单传给后端
    updateMenu2() {
      const data = {
        newMenu: [],
        oldMenu: [],
        roleId: this.roleId
      }
      if (this.menu.length > 0) {
        this.menu.forEach(id => {
          data.newMenu.push(id)
        })
      }
      if (this.menu1.length > 0) {
        this.menu1.forEach(id => {
          data.oldMenu.push(id)
        })
      }
      this.$http.post("/menu/changeRoleMenu", data);
      this.menuWindow = false;
    },
    //更改角色权限
    updatePerm(data) {
      const name = data.name;
      if (this.perm.includes(name)) {
        this.perm = this.perm.filter((item) => {
          return item !== name;
        });
      } else {
        this.perm.push(name)
      }
      //去重
      this.perm = [...new Set(this.perm)];
    },
    //将更改后的权限传给后端
    updatePerm2() {
      const data = {
        newPerm: this.perm,
        oldPerm: this.perm1,
        roleId: this.id
      }
      this.$http.post("/perm/changeRolePerm", data);
      this.permWindow = false;
    },
    //刷新页面
    refresh() {
      this.$GlobalLoading(true, {text: "正在重置搜索条件，请稍候..."});
      setTimeout(() => {
        this.searchData = {};
        this.getDataList();
        this.$GlobalLoading(false, null);
      }, 1000);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selections = selection;
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },

    //查询所有数据
    getDataList() {
      this.dataListFrom = "getDataList";
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/role/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        roleBo: this.searchData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/role/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      });
    },

    //打开编辑页面
    openEditWindow(item, data) {
      //判断时多选框中选择，还是单条点击
      data = item === null ? data : this.selections[0];
      //打开编辑用户界面
      this.editWindow = true;
      //回显数据
      this.editData = this.$CopyObject(data);
    },
    //打开菜单页面
    openMenuWindow(id) {
      this.$http.post("/menu/getListByRole/" + id).then((res) => {
        this.menu = res;
        this.menu1 = JSON.parse(JSON.stringify(res));
      });
      this.$http.post("/menu/getList").then((res) => {
        this.AllMenu = res;
      });
      this.roleId = id
      this.menuWindow = true;
    },
    //打开权限页面
    openPermWindow(id) {
      this.$http.post("/perm/getListByRole/" + id).then((res) => {
        this.perm = res;
        this.perm1 = JSON.parse(JSON.stringify(res));
      });
      this.$http.post("/perm/getList").then((res) => {
        this.AllPerm = res;
      });
      this.roleId = id
      this.permWindow = true;
    },
    //打开新增页面
    openInsertWindow() {
      this.insertWindow = true;
      this.insertData = {}
    },

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },
    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/role")
    },

    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "delete":
          this.deleteRole(null, row);
          break;
        case "menu":
          this.openMenuWindow(row.id);
          break;
        case "perm":
          this.openPermWindow(row.id);
          break;
        default:
          break;
      }
    },
  },
  mounted() {
    //获取表头数据
    this.getProperties();
	  this.setPageSize();
    this.getDataList();
  },
  computed: {
    //获取打印集合
    getPrintData() {
      let data = []
      if (this.selections.length === 0) {
        data = JSON.parse(JSON.stringify(this.dataList));
      } else {
        data = JSON.parse(JSON.stringify(this.selections));
      }
      return data;
		},
		//获取表头
		getProp() {
			return this.properties.filter(item => item.checked);
		}
  }
};
</script>
<style lang="less">
</style>

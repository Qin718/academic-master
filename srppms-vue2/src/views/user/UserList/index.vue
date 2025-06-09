<template>
  <el-card class="box-card">
    <div>

      <!-- 按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button v-button="':UserList:insert'" plain size="mini" type="primary" @click="openInsertWindow">
            新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':UserList:update'" plain size="mini" type="success" @click="openEditWindow" :disabled="single">
            修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':UserList:delete'" plain size="mini" type="danger" @click="deleteUser" :disabled="multiple">
            删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <printTable v-button="':UserList:print'" :data="getPrintData" :properties="properties"/>
        </el-col>

        <right-toolbar :columns="properties" :showSearch.sync="showSearch" @queryTable="getDataList"></right-toolbar>
      </el-row>

      <!-- 数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection"></el-table-column>
        <el-table-column
	        v-for="{prop, label, width} in getProp"
	        :key="prop"
	        :label="label"
	        :prop="prop"
	        :width="width"
	        sortable>
        </el-table-column>
	      <el-table-column label="操作" width="200">
          <template v-slot="scope">
            <el-button size="mini" type="primary" @click="openEditWindow(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteUser(null,scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
	    <!-- 新增按钮打开的页面 -->
	    <el-dialog :visible.sync="insertWindow" title="新增用户">
		    <el-form :model="insertData">
			    <el-form-item label="用户名" label-width="120px">
				    <el-input
					    v-model="insertData.username"
					    placeholder="账号"
					    style="width: 80%"
				    ></el-input>
			    </el-form-item>
			    <el-form-item label="密码" label-width="120px">
				    <el-input
					    v-model="insertData.password"
					    placeholder="密码"
					    style="width: 80%"
					    type="password"
				    ></el-input>
			    </el-form-item>
			    <el-form-item label="姓名" label-width="120px">
				    <el-input
					    v-model="insertData.name"
					    placeholder="姓名"
					    style="width: 80%"
				    ></el-input>
			    </el-form-item>
			    <el-form-item label="角色" label-width="120px">
				    <el-select v-model="insertData.roleId" placeholder="请选择">
					    <el-option
						    v-for="{id,roleName} in roles"
						    :key="id"
						    :label="roleName"
						    :value="id"
					    >
					    </el-option>
				    </el-select>
			    </el-form-item>
			    <el-form-item label="邮箱" label-width="120px">
				    <el-input
					    v-model="insertData.email"
					    placeholder="邮箱"
					    style="width: 80%"
				    ></el-input>
			    </el-form-item>
			    <el-form-item label="手机号码" label-width="120px">
				    <el-input
					    v-model="insertData.mobile"
					    placeholder="手机号码"
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
			    <el-button @click="insertWindow = false;">取 消</el-button>
			    <el-button type="primary" @click="insertUser()">确 定</el-button>
		    </div>
	    </el-dialog>
      <!-- 编辑按钮打开的页面 -->
      <el-dialog :visible.sync="editWindow" title="编辑用户">
        <el-form :model="editData">
	        <el-form-item label="用户名" label-width="120px">
            <el-input
              v-model="editData.username"
              placeholder="账号"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="姓名" label-width="120px">
            <el-input
              v-model="editData.name"
              placeholder="姓名"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="用户角色" label-width="120px">
            <el-select v-model="editData.roleId" placeholder="请选择">
	            <el-option
		            v-for="{id,roleName} in roles"
		            :key="id"
		            :label="roleName"
		            :value="id"
	            >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="邮箱" label-width="120px">
            <el-input
              v-model="editData.email"
              placeholder="邮箱"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="手机号码" label-width="120px">
            <el-input
              v-model="editData.mobile"
              placeholder="手机号码"
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
          <el-button type="primary" @click="updateUser()">更 新</el-button>
        </div>
      </el-dialog>
      <!-- 页号 -->
      <el-pagination
        :current-page="pageIndex"
        :page-size="pageSize"
        :page-sizes="pageSizes"
        :total="totalPage"
        layout="total ,sizes,prev,pager,next,jumper"
        style="margin-top: 30px"
        @size-change="sizeChangeHandle"
        @current-change="CurrentChangeHandle"
      >
      </el-pagination>
    </div>
  </el-card>
</template>
<script>

import {message} from "@/method/message";

export default {
  name: "UserList",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      editData: {}, //编辑数据
      insertData: {},//新增数据

      //集合参数
      dataList: [], //页面展示的数据集合
      roles: [], //角色列表
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

      //其他
      single: true,//非单个禁用
      multiple: true,//非多个禁用
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

    //新增用户
    insertUser() {
      const data = {
        userBo: this.insertData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/user/insert", data).then((res) => {
        if (res != null) {
          this.insertWindow = false; //关闭窗口
          this.cutDataList(res)
        }
      });
    },
    //修改用户
    updateUser() {
      this.editWindow = false;
      const data = {
        userBo: this.editData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/user/update", data).then((res) => {
        this.cutDataList(res);
      });
    },
    //删除用户
    deleteUser(item, data) {
      const list = []
      //判断时多选框中选择，还是单条点击
      if (item === null) {
        list.push(data['userId'])
      } else {
        this.selections.forEach(item => list.push(item['userId']))
      }
      this.$confirm("是否确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
          const data = {
            list: list,
            pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
          }
          // 表示处理的是 删除操作
          this.$http.post("/user/delete", data).then((res) => {
            if (res != null) {
              this.cutDataList(res)
            }
          });
        })
        .catch(() => {
          message.info("已取消删除");
        });
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selections = selection;
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
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
      this.$http.post("/user/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex),
        userBo: this.searchData
      }
      this.$http.post("/user/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      });
    },
    //获取角色信息
    getRoleList() {
      this.$http.post("/role/getList/roleKind").then((res) => {
        this.roles = res;
      });
    },

    //打开编辑页面
    openEditWindow(item) {
      this.editWindow = true; //打开编辑用户界面
      //回显数据
      this.editData = this.$CopyObject(item);
    },
    //打开新增页面
    openInsertWindow() {
	    this.insertWindow = true;
	    this.insertData = {};
    },

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },

    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/user")
    }
  },
  mounted() {
    //获取表头数据
    this.getProperties();
	  this.setPageSize();
    this.getDataList();
    this.getRoleList();
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

<template>
  <el-card class="box-card">
    <div>
      
      <!-- 按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button v-button="':SysUser:insert'" plain size="mini" type="primary" @click="openInsertWindow">
            新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysUser:update'" plain size="mini" type="success" @click="openEditWindow" :disabled="single">
            修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysUser:delete'" plain size="mini" type="danger" @click="deleteUser" :disabled="multiple">
            删除
          </el-button>
        </el-col>
        
        <el-col :span="1.5">
          <printTable v-button="':SysUser:print'" :data="getPrintData" :properties="properties"/>
        </el-col>

        <right-toolbar :columns="properties" :showSearch.sync="showSearch" @queryTable="getDataList"></right-toolbar>
      </el-row>

      <!-- 角色数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column
	        v-for="{prop, label, width} in getProp"
	        :key="prop"
	        :label="label"
	        :prop="prop"
	        :width="width"
	        sortable>
        </el-table-column>
	      <el-table-column label="操作" width="300">
          <template v-slot="scope">
            <el-button size="mini" type="primary" @click="openEditWindow(null,scope.row)">
              编辑
            </el-button>
            <el-button size="mini" type="warning" @click="deleteUser(null,scope.row)">
              删除
            </el-button>
            
          </template>
        </el-table-column>
      </el-table>

      <!-- 新增按钮打开的页面 -->
      <el-dialog title="新增用户" :visible.sync="insertWindow">
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
      <el-dialog title="编辑用户" :visible.sync="editWindow">
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
      <!-- 用户导入对话框 -->
      <el-dialog :visible.sync="importWindow" append-to-body title="导入用户">
        <el-upload
          ref="upload"
          :limit="1"
          accept=".xlsx, .xls"
          action=""
          :disabled="isUploading"
          :on-progress="handleFileUploadProgress"
          :auto-upload="false"
          :show-file-list="true"
          drag
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip text-center" slot="tip">
            <div class="el-upload__tip" slot="tip">
              <el-checkbox v-model="updateSupport" /> 是否更新已经存在的用户数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
          </div>
        </el-upload>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="importWindow = false">取 消</el-button>
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
import cache from "@/method/cache";

export default {
  name: "SysUser",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      insertData: {},//新增数据
      editData: {},//编辑数据

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
      insertWindow: false,
      editWindow: false,
      importWindow:false,

      //上传参数
      isUploading: false,// 是否禁用上传
      updateSupport: 0,// 是否更新已经存在的用户数据

      //其他
      single: true,//非单个禁用
      multiple: true,//非多个禁用
      showSearch: true,// 显示搜索条件
      dataListFrom: "getDataList",//当前数据来源于搜索还是全局

      //表头数据
      properties: [],
    };
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

    //导出用户
    exportUser() {
      const data = []
      if (this.selections.length !== 0){
        this.selections.forEach(item => {
          data.push(item['id'])
        })
      }
      this.$http.post("/export/user", data).then(res => {
        this.$exportExcel(res)
      })
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
    //查询数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        userBo: this.searchData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
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

    //打开添加窗口
    openInsertWindow() {
      this.insertWindow = true;
      this.insertData = {};
    },
    //打开编辑页面
    openEditWindow(item, data) {
      //判断时多选框中选择，还是单条点击
      data = item === null ? data : this.selections[0];
      //打开编辑菜单界面
      this.editWindow = true;
      //清空回显数据
      this.editData = [];
      //回显数据
      this.editData = this.$CopyObject(data);
    },
    //打开导入页面
    openImportWindow(){
      this.importWindow = true;
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
    //新增页面的确认键
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
    //改变账号激活/禁用状态
    changeUserStatus(row) {
      const data = {
        bo: row,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/user/ChangeStatus", data).then((res) => {
	      const username = cache.session.get("username")
	      if (row.username === username) {
          message.warning("您已将账号禁用,请联系管理员或许你可以重新注册一个账号")
          // 清空存储的Token信息
		      cache.session.removeAll();
          // 然后跳转到登录页
          this.$router.push("/");
        }
        this.cutDataList(res);
      });
    },
    //删除用户
    deleteUser(item,data) {
      const list = []
      //判断时多选框中选择，还是单条点击
      if(item === null) {
        list.push(data['id'])
      }else {
        this.selections.forEach(item => list.push(item['id']))
      }
      this.$confirm("是否确认全部删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const data = {
            list: list,
            pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
          }
          this.$http.post("/user/delete", data).then((res) => {
            this.cutDataList(res)
          });
        })
        .catch(() => {
          message.info("已取消删除");
        })
    },

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },

    // 提交上传文件
    submitFileForm() {
      const list = this.$refs.upload['uploadFiles']
      if (list.length > 0) {
        this.$GlobalLoading(true,{ text: '正在上传中，请稍后...',})
        this.importWindow = false;
        this.isUploading = false;
        this.$UploadFile("/import/user", list).then(() => {
          setTimeout(()=>{
            this.getDataList();
            this.$GlobalLoading(false,null)
          },1000)
        })
      }else {
        message.info("请选择文件！")
      }
      this.$refs.upload.clearFiles();
    },
    // 下载模板操作
    importTemplate() {
      this.selections = [0];
      this.exportUser();
    },
    // 文件上传中处理
    handleFileUploadProgress() {
      this.upload.isUploading = true;
    },

    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/user")
    }
  },
  mounted() {
    this.getProperties();
    this.setPageSize();
    this.getDataList();
    this.getRoleList();
  },
};
</script>
<style>
</style>

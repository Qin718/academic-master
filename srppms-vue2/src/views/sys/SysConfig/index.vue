<template>
  <el-card class="box-card">
    <div>
      <!-- 搜索框 -->
      <el-form v-show="showSearch" ref="searchData" :inline="true" :model="searchData" class="demo-form-inline"
               size="small">
        <el-form-item label="编号" style="width: 20%">
          <el-input v-model="searchData['id']" clearable maxlength="64" placeholder="编号"/>
        </el-form-item>
        <el-form-item label="标识码" style="width: 20%">
          <el-input v-model="searchData['key']" clearable maxlength="64" placeholder="标识码"/>
        </el-form-item>
        <el-form-item label="参数内容" style="width: 20%">
          <el-input v-model="searchData['value']" clearable maxlength="64" placeholder="参数内容"/>
        </el-form-item>
        <el-form-item label="备注" style="width: 20%">
          <el-input v-model="searchData['remark']" clearable maxlength="64" placeholder="备注"/>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="getDataListBySearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button v-button="':SysConfig:insert'" plain size="mini" type="primary" @click="openInsertWindow">
            新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysConfig:update'" plain size="mini" type="success" @click="openEditWindow" :disabled="single">
            修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysConfig:delete'" plain size="mini" type="danger" @click="deleteConfig" :disabled="multiple">
            删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <printTable v-button="':SysConfig:print'" :data="getPrintData" :properties="properties"/>
        </el-col>

        <right-toolbar :columns="properties" :showSearch.sync="showSearch" @queryTable="getDataList"></right-toolbar>
      </el-row>
      <!-- 参数数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="50"/>
        <el-table-column
	        v-for="{prop, label, width} in getProp"
	        :key="prop"
	        :label="label"
	        :prop="prop"
	        :width="width * 2"
	        sortable>
        </el-table-column>
	      <el-table-column label="操作" width="200">
          <template v-slot="scope">
            <el-button @click="deleteConfig(null,scope.row)" size="mini" type="warning">
              删除
            </el-button>
            <el-button @click="openEditWindow(scope.row)" size="mini" type="success">
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 新增按钮打开的页面 -->
      <el-dialog :visible.sync="insertWindow" title="新增参数" width="35%">
        <el-form :model="insertData">
          <el-form-item label="参数标识符" label-width="120px">
            <el-input
              v-model="insertData.key"
              placeholder="参数标识符"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="参数内容" label-width="120px">
            <el-input
              v-model="insertData.value"
              placeholder="参数内容"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="备注" label-width="120px">
            <el-input
              v-model="insertData['remark']"
              placeholder="备注"
              style="width: 80%"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="insertWindow = false;">取 消</el-button>
          <el-button type="primary" @click="insertConfig()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 编辑按钮打开的页面 -->
      <el-dialog :visible.sync="updateWindow" title="编辑菜单">
        <el-form :model="editData">
          <el-form-item label="参数标识符" label-width="20%">
            <el-input
              v-model="editData.config"
              placeholder="参数标识符"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="参数内容" label-width="20%">
            <el-input
              v-model="editData.value"
              placeholder="参数内容"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="备注" label-width="20%">
            <el-input
              v-model="editData['remark']"
              placeholder="备注"
              style="width: 80%"
              type="textarea"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="updateWindow = false">取 消</el-button>
          <el-button type="primary" @click="updateConfig()">更 新</el-button>
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
  name: "SysUserOnline",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      editData: {}, //编辑数据
      insertData: {},//新增数据

      //集合参数
      dataList: [], //页面展示的数据集合
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
      updateWindow: false,

      //其他
      single: true,//非单个禁用
      multiple: true,//非多个禁用
      showSearch: true,// 显示搜索条件

      //表头数据
      properties: [],
    };
  },
  methods: {
    //改变数组大小
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.getDataList();
    },
    //改变当前页号
    CurrentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },

    //查询所有数据
    getDataList() {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/config/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        configBo: this.searchData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/config/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      });
    },

    //打开新增页面
    openInsertWindow() {
      this.insertWindow = true;
      this.insertData = {}
    },
    //打开编辑页面
    openEditWindow(item) {
      this.updateWindow = true;
      //回显数据
      this.editData = this.$CopyObject(item);
    },

    //修改参数
    updateConfig() {
      this.updateWindow = false;
      const data = {
        config: this.editData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/config/update", data).then((res) => {
        this.$setGlobalConfig(res);
        this.cutDataList(res);
      });
    },
    //删除参数
    deleteConfig(item, data) {
      const list = []
      //判断时多选框中选择，还是单条点击
      if (item === null) {
        list.push(data.id)
      } else {
        this.selections.forEach(item => list.push(item.id))
      }
      this.$confirm("是否确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const data = {
            list: list,
            pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
          }
          this.$http.post("/config/delete", data).then((res) => {
            this.$setGlobalConfig(res);
            this.cutDataList(res);
          });
        })
        .catch(() => {
          message.info("已取消删除");
        });
    },
    //新增参数
    insertConfig() {
      this.insertWindow = false;
      const data = {
        config: this.insertData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/config/insert", data).then((res) => {
        this.$setGlobalConfig(res);
        this.cutDataList(res);
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

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },

    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/config")
    }
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

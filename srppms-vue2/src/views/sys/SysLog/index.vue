<template>
  <el-card class="box-card">
    <div>
      <!-- 搜索框 -->
      <el-form v-show="showSearch" ref="searchData" size="small" :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item label="日志编号" style="width: 23%">
          <el-input v-model="searchData['id']" placeholder="日志编号" clearable/>
        </el-form-item>
        <el-form-item label="操作账户" style="width: 23%">
          <el-input v-model="searchData['account']" placeholder="操作账户" clearable/>
        </el-form-item>
        <el-form-item label="请求地址" style="width: 23%">
          <el-input v-model="searchData['url']" placeholder="请求地址" clearable/>
        </el-form-item>
        <el-form-item label="请求接口" style="width: 23%">
          <el-input v-model="searchData['uri']" placeholder="请求接口" clearable/>
        </el-form-item>
        <el-form-item label="请求参数" style="width: 23%">
          <el-input v-model="searchData['params']" placeholder="请求参数" clearable/>
        </el-form-item>
        <el-form-item label="设备来源" style="width: 23%">
          <el-input v-model="searchData['mobile']" placeholder="设备来源" clearable/>
        </el-form-item>
        <el-form-item label="请求来源" style="width: 23%">
          <el-input v-model="searchData['innerIp']" placeholder="请求来源" clearable/>
        </el-form-item>
        <el-form-item label="方法名称" style="width: 23%">
          <el-input v-model="searchData['methodApi']" placeholder="方法名称" clearable/>
        </el-form-item>
        <el-form-item label="类名称" style="width: 23%">
          <el-input v-model="searchData['classApi']" placeholder="类名称" clearable/>
        </el-form-item>
        <el-form-item label="ip地址" style="width: 23%">
          <el-input v-model="searchData['ip']" placeholder="ip地址" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="getDataListBySearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button v-button="':SysLog:delete'" plain size="mini" type="danger" @click="deleteLog" :disabled="multiple">
            删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <printTable v-button="':SysLog:print'" :data="getPrintData" :properties="properties"/>
        </el-col>

        <right-toolbar :columns="properties" :showSearch.sync="showSearch" @queryTable="getDataList"></right-toolbar>
      </el-row>

      <!-- 数据表单 -->
      <el-table :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="50"/>
        <el-table-column
	        v-for="{prop, label, width} in getProp"
	        :key="prop"
	        :label="label"
	        :prop="prop"
	        :width="width"
	        sortable>
        </el-table-column>
      </el-table>
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

export default {
  name: "SysLog",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据

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

      //其他
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

    //刷新页面
    refresh() {
      this.$GlobalLoading(true, {text: "正在重置搜索条件，请稍候..."});
      setTimeout(() => {
        this.searchData = {};
        this.getDataList();
        this.$GlobalLoading(false, null);
      }, 1000);
    },

    //查询所有数据
    getDataList() {
      this.dataListFrom = "getDataList";
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/log/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex),
        bo: this.searchData
      }
      this.$http.post("/log/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      });
    },

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selections = selection;
      this.multiple = !selection.length;
    },

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },

    //删除日志
    deleteLog() {
      const list = []
      //判断时多选框中选择，还是单条点击
      this.selections.forEach(item => list.push(item.id))
      const data = {
        list: list,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/log/delete", data).then((res) => {
        //分页操作
        this.cutDataList(res)
      });
    },

    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/log")
    }
  },
  mounted() {
    //获取表头数据
    this.getProperties();
	  this.setPageSize();
    this.getDataList();
  },
};
</script>

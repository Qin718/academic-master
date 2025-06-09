<template>
  <el-card class="box-card">
    <div>
      <!-- 搜索框 -->
      <el-form v-show="showSearch" ref="searchData" size="small" :inline="true" :model="searchData" class="demo-form-inline">
        <el-form-item label="公告编号" style="width: 23%">
          <el-input v-model="searchData['noticeId']" placeholder="公告编号" clearable/>
        </el-form-item>
        <el-form-item label="公告标题" style="width: 23%">
          <el-input v-model="searchData.title" placeholder="公告标题" clearable/>
        </el-form-item>
        <el-form-item label="公告内容" style="width: 23%">
          <el-input v-model="searchData.content" placeholder="公告内容" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="getDataListBySearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button v-button="':SysNotices:insert'" plain size="mini" type="primary" @click="openInsertWindow">
            新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysNotices:update'" plain size="mini" type="success" :disabled="single" @click="openEditWindow">
            修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysNotices:delete'" plain size="mini" type="danger" :disabled="multiple" @click="deleteNotice">
            删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <printTable v-button="':SysNotices:print'" :data="getPrintData" :properties="properties"/>
        </el-col>

        <right-toolbar :columns="properties" :showSearch.sync="showSearch" @queryTable="getDataList"></right-toolbar>
      </el-row>

      <!-- 角色数据表单 -->
      <el-table :data="dataList" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40"></el-table-column>
        <el-table-column
	        v-for="{prop, label, width} in getProp"
	        :key="prop"
	        :label="label"
	        :prop="prop"
	        :width="width * 1.5"
	        sortable>
          <!-- 图标 -->
          <template v-if="prop==='icon'" v-slot="scope">
            <i :class="`el-icon-${scope.row.icon}`"></i>
          </template>
        </el-table-column>
      </el-table>
      <!-- 新增按钮打开的页面 -->
      <el-dialog title="新增系统公告" :visible.sync="insertWindow">
        <el-form :model="insertData">
          <el-form-item label="公告标题" label-width="120px">
            <el-input
              v-model="insertData.title"
              placeholder="系统标题"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="公告图标" label-width="120px">
            <el-select v-model="insertData.icon" placeholder="请选择">
              <el-row style="width: 300px;">
                <el-col v-for="item in icons" :key="item[0]['icon']" :span="6">
                  <el-option v-for="{icon,name} in item" :key="icon" :label="name" :value="icon" class="el-option">
                    <i :class="`el-icon-${icon}`"/>
                  </el-option>
                </el-col>
              </el-row>
            </el-select>
          </el-form-item>
          <el-form-item label="公告内容" label-width="120px">
            <el-input
              type="textarea"
              v-model="insertData.content"
              placeholder="公告内容"
              style="width: 80%"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="insertWindow = false">取 消</el-button>
          <el-button type="primary" @click="insertNotice()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 编辑按钮打开的页面 -->
      <el-dialog title="编辑系统公告" :visible.sync="editWindow">
        <el-form :model="editData">
          <el-form-item label="公告标题" label-width="120px">
            <el-input
              v-model="editData.title"
              placeholder="系统标题"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="公告图标" label-width="120px">
            <el-select v-model="editData.icon" placeholder="请选择">
              <el-row style="width: 300px;">
                <el-col v-for="item in icons" :key="item[0]['icon']" :span="6">
                  <el-option v-for="{icon,name} in item" :key="icon" :label="name" :value="icon" class="el-option">
                    <i :class="`el-icon-${icon}`"/>
                  </el-option>
                </el-col>
              </el-row>
            </el-select>
          </el-form-item>
          <el-form-item label="公告内容" label-width="120px">
            <el-input
              type="textarea"
              v-model="editData.content"
              placeholder="公告内容"
              style="width: 80%"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editWindow = false">取 消</el-button>
          <el-button type="primary" @click="updateNotice()">更 新</el-button>
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

export default {
  name: "SysNotices",
  data() {
    return {
      //数据参数
      insertData: {},//新增数据
      searchData: {},//搜索数据
      editData: {},//编辑数据

      //集合参数
      icons: [], //图标集合
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
      editWindow: false,

      //其他
      single: true,//非单个禁用
      multiple: true,//非多个禁用
      showSearch: true,// 显示搜索条件
      dataListFrom: "getDataList",

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

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },

    //查询所有数据
    getDataList() {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/notice/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res)
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        noticesBo: this.searchData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/notice/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      });
    },
    //获取公告图标字典
    getIcon() {
      this.$http.post("/dict/getDictVoList/icon").then(res => {
        this.icons = this.$StrToJson(res);

        const len = this.icons.length //数组长度
        const num = 4 //数组数量
        const arr = []
        for (let i = 0; i < len; i++) {
          if (i < num) {
            arr.push([])
          }
          arr[i % num].push(this.icons[i])
        }
        this.icons = arr
      })
    },

    //添加公告
    insertNotice() {
      //关闭窗口
      this.insertWindow = false;
      const data = {
        noticesBo: this.insertData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/notice/insert", data).then((res) => {
        this.cutDataList(res);
      });
    },
    //修改公告
    updateNotice(){
      //关闭窗口
      this.editWindow = false;
      const data = {
        noticesBo: this.editData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/notice/update", data).then((res) => {
        this.cutDataList(res);
      });
    },
    //删除公告
    deleteNotice(item,item1) {
      const list = []
      //判断时多选框中选择，还是单条点击
      if(item === null) {
        list.push(item1.id)
      }else {
        this.selections.forEach(item => list.push(item.id))
      }

      const data = {
        list: list,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/notice/delete", data).then((res) => {
        this.cutDataList(res)
      });
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

    //打开新增页面
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

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },

    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/notice")
    }
  },
  mounted() {
    //获取表头数据
    this.getProperties();
	  this.setPageSize();
    this.getIcon();
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

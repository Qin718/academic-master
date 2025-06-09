<template>
  <el-card class="box-card">
    <div>
      <!-- 搜索框 -->
      <el-form :model="searchData" ref="searchData" :inline="true" class="demo-form-inline">
        <el-form-item label="项目名称">
          <el-input
            v-model="searchData.name"
            clearable
            placeholder="项目名称"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataListBySearch" icon="el-icon-search" type="primary">
            查询
          </el-button>
        </el-form-item>
      </el-form>
      <!-- 数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="项目编号" prop="projectId" width="80">
        </el-table-column>
        <el-table-column label="项目名称" prop="project.name" width="150">
        </el-table-column>
        <el-table-column label="项目审核状态" prop="status" width="120">
        </el-table-column>
        <el-table-column label="项目创建人" prop="user.username" width="150">
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="180">
        </el-table-column>
        <el-table-column
          label="最近一次的修改时间"
          prop="updateTime"
          width="180"
        >
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button @click="openLookWindow(scope.row)" size="mini" type="primary">
              查看
            </el-button>
            <el-button @click="openProcessWindow(scope.row)" size="mini" type="success">
              审核
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 查看打开的界面 -->
      <el-dialog
        :model="projectData"
        :visible.sync="lookWindow"
        title="项目详情"
        width="50%"
      >
        <el-descriptions title="项目信息">
          <el-descriptions-item label="项目编号">
            {{ this.projectData.id }}
          </el-descriptions-item>
          <el-descriptions-item label="项目名称">
            {{ this.projectData.name }}
          </el-descriptions-item>
          <el-descriptions-item label="项目类型">
            {{ this.projectData.type }}
          </el-descriptions-item>
          <el-descriptions-item label="项目状态">
            {{ this.projectData.status }}
          </el-descriptions-item>

          <el-descriptions-item label="创建人">
            <el-tag size="small">
              {{ this.userData.name }}
              </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="项目说明">
              {{this.projectData['remark']}}
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions column:2>
          <el-descriptions-item label="申请资金">
            {{this.projectData['money']}}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{this.projectData.createTime}}
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions>
          <el-descriptions-item label="最近一次修改时间" prop="updateTime">
            {{this.projectData.updateTime}}
          </el-descriptions-item>
        </el-descriptions>
        <el-divider></el-divider>
        <el-descriptions title="项目创建者信息">
          <el-descriptions-item label="账号">
            {{ this.userData.username }}
          </el-descriptions-item>
          <el-descriptions-item label="姓名">
            <el-tag size="small">
             {{ this.userData.name }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
          {{this.userData.email}}
          </el-descriptions-item>
          <el-descriptions-item label="手机号码">
            {{this.userData.mobile}}
          </el-descriptions-item>
          <el-descriptions-item label="账号状态">
              {{this.userData.status === 0 ? "激活" : "禁用"}}
          </el-descriptions-item>
        </el-descriptions>
      </el-dialog>
      <!-- 审核按钮打开的页面 -->
      <el-dialog
        :model="projectData"
        :visible.sync="processWindow"
        title="审核项目"
        width="50%"
      >
        <el-descriptions title="项目信息">
          <el-descriptions-item label="项目编号">
            {{ this.projectData.id }}
          </el-descriptions-item>
          <el-descriptions-item label="项目名称">
            {{ this.projectData.name }}
          </el-descriptions-item>
          <el-descriptions-item label="项目类型">
            {{ this.projectData.type }}
          </el-descriptions-item>
          <el-descriptions-item label="项目状态">
              {{this.projectData.status}}
          </el-descriptions-item>

          <el-descriptions-item label="创建人">
            <el-tag size="small">
             {{ this.userData.name }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="项目说明">
            {{this.projectData['remark']}}
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions column:2>
          <el-descriptions-item label="创建时间">
              {{this.projectData.createTime}}
          </el-descriptions-item>
          <el-descriptions-item label="申请资金">
              {{this.projectData['money']}}
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions>
          <el-descriptions-item label="最近一次修改时间" prop="updateTime">
            {{this.projectData.updateTime}}
          </el-descriptions-item>
        </el-descriptions>
        <el-divider></el-divider>
        <!-- 审核结果和审核意见 -->
        <el-form :model="process">
          <el-form-item label="审核结果" label-width="120px">
            <el-select v-model="process.process" :placeholder="'未 审 核'">
              <el-option label="审 核 不 通 过" value="审核不通过"></el-option>
              <el-option label="审  核  通  过" value="审核通过"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="审核意见" label-width="120px">
            <el-input
              v-model="process['remark']"
              placeholder="审核意见"
              style="width: 80%"
              type="textarea"
            ></el-input>
          </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="processWindow = false">取 消</el-button>
          <el-button type="primary" @click="updateProcess()">提 交</el-button>
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
  name: "ProcessProject",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      projectData: {},//查看数据
      userData: {},//创建者数据
      process: {},//审核数据

      //集合参数
      dataList: [], //页面展示的数据集合

      //页号参数
      pageIndex: 1,
      pageSize: 5,
      pageSizes: [],
      totalPage: 0,
      begin: 0,
      end: this.pageSize - 1,

      //窗口显示参数
      processWindow: false,
      lookWindow: false,

      //其他
      dataListFrom: "getDataList",//当前数据来源于搜索还是全局
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

    //审核项目
    updateProcess() {
      this.processWindow = false;
      const data = {
        processBo: this.process,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/process/updateProcess", data).then((res) => {
        this.cutDataList(res);
      });
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
      this.$http.post("/item/getPageVo/Process", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex),
        projectBo: this.searchData
      }
      this.$http.post("/item/getPageVo/Process/search", data).then((res) => {
        this.cutDataList(res);
      });
    },

    //打开查看页面
    openLookWindow(item) {
      //打开项目详细界面
      this.lookWindow = true;
      this.projectData = item.project;
      this.userData = item.user;
      this.projectData.status = item.status
      this.projectData.createTime = item.createTime
      this.projectData.updateTime = item.updateTime
    },
    //打开审核页面
    openProcessWindow(item) {
      this.process = item.process;
      //打开界面
      this.processWindow = true;
      this.projectData = item.project;
      this.userData = item.user;
      this.projectData.status = item.status
      this.projectData.createTime = item.createTime
      this.projectData.updateTime = item.updateTime
    },

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    }
  },
  mounted() {
	  this.setPageSize();
    this.getDataList();
  },
};
</script>

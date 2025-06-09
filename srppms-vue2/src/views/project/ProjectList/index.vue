<template>
  <el-card class="box-card">
    <div>
      <!-- 搜索框 -->
      <el-form
        ref="searchData"
        :inline="true"
        :model="searchData"
        class="demo-form-inline"
      >
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
        <el-form-item>
          <el-button type="success" @click="openInsertWindow">
            新增
          </el-button
          >
        </el-form-item>
      </el-form>
      <!-- 角色数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border>
        <!--        <el-table-column type="selection" width="39"></el-table-column>-->
        <el-table-column label="项目编号" prop="projectId" width="80">
        </el-table-column>
        <el-table-column label="项目名称" prop="project.name" width="150">
          <template v-slot="scope">
            <span>{{ scope.row.project.name }}</span>
	          <el-tooltip v-if="scope.row.score >= hot"
	                      content="srppms 热门项目"
	                      effect="light"
	                      placement="top-start"
	                      style="color: red">
              <i class="el-icon-star-on"></i>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="项目类型" prop="project.type" width="150">
        </el-table-column>
        <el-table-column label="项目审核状态" prop="status" width="120">
          <template v-slot="scope">
            <span>{{ scope.row.status === null ? "没有分配审核" : scope.row.status }}</span>
          </template>
        </el-table-column>
        <el-table-column label="项目创建人" prop="user.username" width="150">
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160">
        </el-table-column>
        <el-table-column
          label="最近一次的修改时间"
          prop="updateTime"
          width="160"
        >
        </el-table-column>
        <el-table-column label="操作" width="110px">
          <template v-slot="scope">
            <el-button @click="openLookWindow(scope.row)" size="mini" type="primary">
              查看项目
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="评分">
          <template v-slot="scope">
            <div class="block" @click="getItemScore(scope.row)">
              <el-rate
                :colors="colors"
                :high-threshold="high_threshold"
                :low-threshold="low_threshold"
                :max="10"
                :value="scope.row.score"
                allow-half
                show-score
                text-color="green"
                @change="getScore"
              >
              </el-rate>
            </div>
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
            {{ this.projectData.status === null ? "没有分配审核" : this.projectData.status }}
          </el-descriptions-item>
          <el-descriptions-item label="项目创建人">
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
          {{ this.projectData.createTime }}
          </el-descriptions-item>
          <el-descriptions-item label="申请资金">
          {{this.projectData['money']}}
          </el-descriptions-item>
        </el-descriptions>
        <el-descriptions>
          <el-descriptions-item label="最近一次修改时间" prop="updateTime">
          {{ this.projectData.updateTime }}
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
          {{ this.userData.email }}
          </el-descriptions-item>
          <el-descriptions-item label="手机号码">
          {{ this.userData.mobile }}
          </el-descriptions-item>
          <el-descriptions-item label="账号状态">
          {{ this.userData.status === 1 ? "正常" : "禁用" }}
          </el-descriptions-item>
        </el-descriptions>
      </el-dialog>
      <!-- 新增按钮打开的页面 -->
      <el-dialog :visible.sync="insertWindow" title="创建项目">
        <el-form :model="insertData">
          <el-form-item label="项目名称" label-width="120px">
            <el-input
              v-model="insertData.name"
              placeholder="项目名称"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="项目内容" label-width="120px">
            <el-input
              v-model="insertData.content"
              placeholder="项目内容"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="项目类型" label-width="120px">
            <el-input
              v-model="insertData.type"
              placeholder="项目类型"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="申请资金" label-width="120px">
            <el-input
              v-model="insertData['money']"
              placeholder="申请资金"
              style="width: 80%"
            ></el-input>
          </el-form-item>
          <el-form-item label="项目说明" label-width="120px">
            <el-input
              v-model="insertData['remark']"
              style="width: 80%"
              type="textarea"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="insertWindow = false;">取 消</el-button>
          <el-button type="primary" @click="checkItemBeforeInsert()">确 定</el-button>
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
  name: "ProjectList",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      insertData: {},//新增数据
      projectData: {},//查看数据
      userData: {},//创建者数据

      //评分参数
      score: 0, //要修改的评分
      low_threshold: 4, //低分和中分分界点，此分数包含在低分中
      high_threshold: 7, //低分和高分分界点，此分数包含在高分中
      colors: ["#99A9BF", "#F7BA2A", "#EB5E26"],
	    hot: parseFloat(cache.session.get('hot')),//热门项目的标准

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
      insertWindow: false,
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

    //用户评分
    getScore(value) {
      this.score = value;
    },
    //提交用户评分
    getItemScore(item) {
      const score = {
        id: item.projectId,
        score: this.score
      }
      this.$http.post("/project/score", score).then(() => {
        this.getDataList()
      });
    },
    //检查创建项目时是否有未填项
    checkItemBeforeInsert() {
      if (this.insertData['money'] === "") {
        this.$confirm("当前申请资金为0,是否继续创建项目？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.insertProject()
        }).catch(() => {
          message.info("已取消创建项目");
        });
      } else {
        this.insertProject()
      }
    },
    //新增页面的确认键
    insertProject() {
      const data = {
        projectBo: this.insertData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/item/insert", data).then((res) => {
        this.cutDataList(res)
        this.insertWindow = false
      });
    },

    //查询所有数据
    getDataList() {
      this.dataListFrom = "getDataList";
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/item/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        projectBo: this.searchData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/item/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      });
    },

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },

    //查看按钮
    openLookWindow(item) {
      this.lookWindow = true;
      this.projectData = item.project;
      this.userData = item.user;
      this.projectData.status = item.status
      this.projectData.createTime = item.createTime
      this.projectData.updateTime = item.updateTime
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
    }
  },
  mounted() {
	  this.setPageSize();
    this.getDataList();
  },
};
</script>

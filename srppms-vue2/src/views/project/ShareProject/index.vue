<template>
  <el-card class="box-card">
    <div>
      <!-- 角色数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="项目编号" prop="projectId" width="80">
        </el-table-column>
        <el-table-column label="项目名称" prop="project.name" width="150">
        </el-table-column>
        <el-table-column label="项目审核状态" prop="status" width="110">
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
            <el-button
              size="mini"
              type="primary"
              @click="openLookWindow(scope.row)"
            >
              查看项目
            </el-button>
            <el-button
              size="mini"
              type="success"
              @click="openShareWindow(scope.row)"
            >
              分配审核人员
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 查看打开的界面 -->
      <el-dialog
        :model="projectData"
        :visible.sync="dialogFormVisibleLook"
        title="项目详情"
        width="40%"
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
          <el-descriptions-item label="申请资金">
            {{this.projectData['money']}}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ this.projectData.createTime }}
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
      <!-- 分配按钮打开的页面 -->
      <el-dialog
        :visible.sync="shareWindow"
        title="分配审核人员"
        width="50%"
      >
        <!-- 穿梭表 -->
        <div style="text-align: center">
          <el-transfer
            v-model="value"
            :button-texts="['取消分配', '分配项目']"
            :data="data"
            :format="{
              noChecked: '${total}',
              hasChecked: '${checked}/${total}'
            }"
            :titles="['可选的审核人员', '已分配的审核人员']"
            filterable
            style="text-align: left; display: inline-block"
            @change="selectChange()"
          >
          </el-transfer>
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
  name: "ShareProject",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      projectData: {},//查看数据
      userData: {},//创建者数据

      //集合参数
      dataList: [], //页面展示的数据集合
      shareUser: [], //已经分配给该项目的审核人员
      allShareUser: [], //所有审核人员
      value: [],
      data: [],

      //页号参数
      pageIndex: 1,
      pageSize: 5,
      pageSizes: [],
      totalPage: 0,
      begin: 0,
      end: this.pageSize - 1,

      //窗口显示参数
      dialogFormVisibleLook: false, //查看界面
      shareWindow: false, //分配审核人员界面

      //其他
      projectId: 0,//存储审核项目ID
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
      this.$http.post("/item/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },

    //打开查看页面
    openLookWindow(item) {
      //打开项目详细界面
      this.dialogFormVisibleLook = true;
      this.projectData = item.project;
      this.userData = item.user;
      this.projectData.status = item.status
      this.projectData.createTime = item.createTime
      this.projectData.updateTime = item.updateTime
    },

    //分配审核人员按钮
    openShareWindow(item) {
      //打开界面
      this.shareWindow = true;
      //存储审核项目ID
      this.projectId = item.projectId
      //获取审核人员名单
      this.getShare(item);
    },
    //获取该项目审核人
    getShare(item) {
      const disabledId = item["createBy"];
      this.$http.post("/process/getList/shareUser/" + item.projectId).then((res) => {
        this.shareUser = res;
        this.generateData(disabledId);
      });
    },
    //获取搜索审核人
    getAllUser() {
      this.$http.post("/process/getList/allShareUser").then((res) => {
        this.allShareUser = res;
      });
    },
    //提交右侧数据
    selectChange() {
      this.shareWindow = false;
      const data = {
        projectId: this.projectId,
        userIdS: this.value,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/process/changeUserProcess", data).then((res) => {
        this.cutDataList(res);
      })
    },
    //初始化量测数据
    generateData(disabledId) {
      this.data.length = 0;
      this.allShareUser.forEach((item) => {
        this.data.push({
          label: item.username,
					key: item.id,
          disabled: disabledId === item['userId'],
        });
      });
      this.value.length = 0;
      this.shareUser.forEach((id) => {
        this.value.push(id);
      });
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
    this.getAllUser();
  },
};
</script>

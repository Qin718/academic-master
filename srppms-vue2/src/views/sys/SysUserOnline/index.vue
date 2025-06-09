<template>
  <el-card class="box-card">
    <div>
      <!-- 角色数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border>
        <el-table-column label="用户账号" prop="account" width="110">
        </el-table-column>
        <el-table-column label="用户名" prop="username" width="110">
        </el-table-column>
        <el-table-column label="登录设备" prop="mobile" width="80">
        </el-table-column>
        <el-table-column label="登录过期时间" prop="loginTimeout" width="150">
        </el-table-column>
        <el-table-column label="token" prop="token" width="350">
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <el-button @click="forcedRetreat(scope.row)" size="mini" type="warning">
              强退
            </el-button>
          </template>
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
  name: "SysUserOnline",
  data() {
    return {
      //集合参数
      dataList: [], //页面展示的数据集合

      //页号参数
      pageIndex: 1,
      pageSize: 5,
      pageSizes: [],
      totalPage: 0,
      begin: 0,
      end: this.pageSize - 1,
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

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },

    //查询所有数据
    getDataList() {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/user/Online/getList", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },

    //强退用户
    forcedRetreat(item) {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/login/forcedOut/" + item.account, pageInfo).then((res) => {
        this.cutDataList(res);
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
  },
};
</script>

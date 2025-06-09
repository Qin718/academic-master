<template>
  <el-card class="box-card">
    <div>
      <!-- 角色数据表单 -->
      <el-table :data="dataList" border>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="编号" prop="id" width="100">
        </el-table-column>
        <el-table-column label="用户账号" prop="account" width="180">
        </el-table-column>
        <el-table-column label="用户名" prop="username" width="180">
        </el-table-column>
        <el-table-column label="登录时间" prop="time" width="260">
        </el-table-column>
        <el-table-column label="登录IP" prop="ip" width="180">
        </el-table-column>
        <el-table-column label="登录设备" prop="mobile" width="180">
        </el-table-column>
        <el-table-column label="登录IP" prop="innerIp" width="180">
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
  name: "SysLogin",
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

    //查询所有数据
    getDataList() {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/login/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
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
    }
  },
  mounted() {
	  this.setPageSize();
    this.getDataList();
  },
};
</script>

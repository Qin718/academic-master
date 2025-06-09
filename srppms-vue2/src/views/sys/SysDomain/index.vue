<template>
  <el-card class="box-card">
    <div>
      <el-collapse accordion>
        <el-collapse-item :title="name"  v-for="{name, apiValue, tableValue, field} in dataList" :key="name" :name="name">
          <span>实体名称：{{apiValue}}</span><br>
          <span>数据库表：{{tableValue}}</span>
          <el-table :data="field" border>
            <el-table-column label="名称" prop="name" width="300">
            </el-table-column>
            <el-table-column label="类型" prop="type" width="150">
            </el-table-column>
            <el-table-column label="说明" prop="remark" width="240">
            </el-table-column>
          </el-table>
        </el-collapse-item>
      </el-collapse>
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
  name: "SysDomain",
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
      this.$http.post("/domain/getPageVo", pageInfo).then((res) => {
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

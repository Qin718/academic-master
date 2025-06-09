<template>
  <el-card class="box-card">
    <div>
      <!-- 角色数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border>
        <el-table-column label="表名注释" prop="tableComment" width="150">
        </el-table-column>
        <el-table-column label="表名称" prop="tableName" width="130">
        </el-table-column>
        <el-table-column label="表数据引擎" prop="engine" width="100">
        </el-table-column>
        <el-table-column label="表字符集" prop="tableCollation" width="180">
        </el-table-column>
        <el-table-column label="数据条数" prop="tableRows" width="130">
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button @click="genJava(scope.row['tableName'])" size="mini" type="primary">
              生成代码
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
	name: "java",
  data() {
    return {
      src: "",
      dataList: [], //页面展示的数据集合
      pageIndex: 1,
      pageSize: 5,
      pageSizes: [],
      totalPage: 0,
      begin: 0,
      end: this.pageSize - 1,
    }
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
      this.dataList = [];
      this.getDataList();
    },
    //将数据分配到数组中
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },
    getDataList() {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/gen/getList", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },
    //生成代码
    genJava(item) {
      this.$http.post("/gen/" + item);
    },
  },
  mounted() {
	  this.setPageSize();
    this.getDataList();
  }
}
</script>
<style lang="less" scoped>

</style>

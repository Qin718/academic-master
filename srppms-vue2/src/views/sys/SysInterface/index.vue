<template>
  <el-card class="box-card">
    <div>
      <!-- 按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5">
          <printTable v-button="':SysInterface:print'" :data="getPrintData" :properties="properties"/>
        </el-col>

        <right-toolbar :columns="properties" :searchButton="false" @queryTable="getDataList"></right-toolbar>
      </el-row>

      <!-- 参数数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border>
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
  name: "SysInterface",
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

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
    },

    //查询所有数据
    getDataList() {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/interface/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },

    //初始化页号
    setPageSize() {
      this.pageSizes = this.$getPageSize()
      this.pageSize = this.pageSizes[0]
    },

    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/interface")
    }
  },
  computed: {
    //获取打印集合
    getPrintData() {
      return JSON.parse(JSON.stringify(this.dataList));
		},
		//获取表头
		getProp() {
			return this.properties.filter(item => item.checked);
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

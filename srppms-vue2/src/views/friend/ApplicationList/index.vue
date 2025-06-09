<template>
  <el-card class="box-card">
    <div>
      <!-- 角色数据表单 -->
      <div>
        <el-row class="el-row">
          <el-col
            v-for="item in dataList"
            :key="item['userA']"
            :offset="1"
            :span="10"
          >
            <el-card class="box-card">
              <div class="user">
                <img id="picture" :src="'data:image/png;base64,'+item.image" alt=""/>
                <div class="userinfo">
                  <p>
	                  账号:<span>{{item['userA']}}</span>
                    <br/>
                    名称:<span>{{ item.notes === "" || item.notes === null ? item.name : item.notes }}</span>
                  </p>
                  <br/>
                  <el-button v-if="item.status===1" @click="handleChangeStatus(item, 2)" class="button" type="primary">
                    同意申请
                  </el-button>
                  <el-button v-if="item.status===2" :disabled=true class="button" type="primary">
                    已同意申请
                  </el-button>
                  <el-button v-if="item.status===1" @click="handleChangeStatus(item, 3)" class="button" type="warning">
                    拒绝申请
                  </el-button>
                  <el-button v-if="item.status===3" :disabled=true class="button" type="warning">
                    已拒绝申请
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
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
  name: "ApplicationList",
  data() {
    return {
      //数据参数
      dataRight: {},
      formRight: {},
      application: {},

      //集合参数
      dataList: [], //页面展示的数据集合

      //页号参数
      pageIndex: 1,
      pageSize: 0,
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

    //申请信息
    getDataList() {
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/friend/getPageVo/App", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },

    //分页操作
    cutDataList(res) {
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
      this.dataList.forEach(f => {
          f.image = this.$unzip(f["imageByte"]["byte"])
      })
    },

    //好友申请 2 同意 3 拒绝
    handleChangeStatus(item, status) {
	    const data = this.$CopyObject(item)
	    data.status = status
	    data.image = ""
	    data.imageByte = ""
	    this.$http.post("/friend/update", data);
	    this.dataList.filter(data => data === item).forEach(data => {
		    data.status = status
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
<style lang="less" scoped>
.el-row {
  min-height: 500px;
  height: 100%;
}

.user {
  border-bottom: 1px solid #999;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  // padding-top: ;
  .userinfo {
    margin-left: 40px;

    .name {
      font-size: 32px;
      margin-bottom: 10px;
    }

    .access {
      color: #999;
      margin-top: 0;
    }
  }

  img {
    margin-left: 40px;
    width: 150px;
    height: 150px;
    border-radius: 50%;
  }
}

.login-info {
  p {
    font-size: 14px;
    color: #999999;
    line-height: 28px;

    span {
      color: #666666;
      margin-left: 20px;
    }
  }
}
</style>

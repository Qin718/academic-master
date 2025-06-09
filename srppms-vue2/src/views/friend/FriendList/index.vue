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
        <el-form-item label="搜索好友">
          <el-input
            v-model="searchData.notes"
            clearable
            placeholder="账号/姓名/备注"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            type="primary"
            @click="getDataListBySearch"
          >查询
          </el-button
          >
        </el-form-item>
      </el-form>
      <!-- 角色数据表单 -->
	    <el-row>
		    <el-col
			    v-for="item in dataList"
			    :key="item['userB']"
			    :offset="1"
			    :span="10"
		    >
			    <el-card class="box-card" style="margin-top: 10px;">
				    <div class="user">
					    <img id="picture" :src="'data:image/png;base64,'+item.image" alt=""/>
					    <div class="userinfo">
						    <p>
							    账号:<span>{{item['userB']}}</span>
							    <br/>
							    名称:<span>{{item.notes === "" || item.notes === null ? item.name : item.notes}}</span>
						    </p>
						    <br/>
						    <el-button :disabled=false class="button" type="primary" @click="chatFriend(item)">
							    私聊
						    </el-button>
						    <el-button class="button" type="success" @click="openEditWindow(item)">
							    备注
						    </el-button>
						    <el-button class="button" type="danger" @click="deleteFriend(item)">
							    删除
						    </el-button>
					    </div>
				    </div>
			    </el-card>
		    </el-col>
	    </el-row>
      <!-- 编辑按钮打开的页面 -->
      <el-dialog
        :visible.sync="editWindow"
        title="编辑备注"
      >
        <el-form :model="editData">
          <el-form-item label="备注" label-width="120px">
            <el-input
              v-model="editData.notes"
              placeholder=""
              style="width: 80%"
            ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editWindow = false">取 消</el-button>
          <el-button type="primary" @click="changeNote">保 存</el-button>
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

export default {
  name: "FriendList",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      editData: {}, //编辑数据
      projectData: {},//查看数据
      userData: {},//创建者数据

      //集合参数
      dataList: [], //页面展示的数据集合

      //页号参数
      pageIndex: 1,
      pageSize: 0,
      pageSizes: [],
      totalPage: 0,
      begin: 0,
      end: this.pageSize - 1,

      //窗口显示参数
      editWindow: false, //默认关闭编辑好友界面

      //其他
      notes: "",
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

    //删除好友
    deleteFriend(item) {
      const data = {
        friendBo: item,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/friend/delete", data).then((res) => {
        this.cutDataList(res)
      })
    },
    //私聊好友
    chatFriend(item) {
      message.info("功能正在开发中，欢迎投稿...")
    },
    //修改好友备注
    changeNote() {
      //关闭编辑好友界面
      this.editWindow = false;
	    this.editData.image = "";
	    this.editData.imageByte = "";
	    this.$http.post("/friend/update", this.editData);
	    this.dataList.filter(data => data['userIdA'] === this.editData['userIdA'] && data['userIdB'] === this.editData['userIdB']).forEach(data => {
		    data.notes = this.editData.notes
	    });
    },

    //分页操作
    cutDataList(res) {
      this.dataList = []
      const data = this.$cutPageDataList(res)
      this.dataList = data.dataList
      this.totalPage = data.totalPage
      this.dataList.forEach(f => {
        f.image = this.$unzip(f["imageByte"]["byte"])
      })
    },

    //查询所有数据
    getDataList() {
      this.dataListFrom = "getDataList";
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/friend/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        friendBo: this.searchData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/friend/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      });
    },

    //打开编辑页面
    openEditWindow(item) {
      this.editWindow = true; //打开编辑好友界面
      //回显数据
      this.editData = this.$CopyObject(item);
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

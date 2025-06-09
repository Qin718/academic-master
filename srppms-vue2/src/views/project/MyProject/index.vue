<template>
  <el-card class="box-card">
    <div>
      <!-- 搜索框 -->
      <el-form :model="searchData" ref="searchData" :inline="true" class="demo-form-inline">
        <el-form-item label="项目名称">
          <el-input v-model="searchData.name" clearable placeholder="项目名称" />
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataListBySearch" icon="el-icon-search" type="primary">
            查询
          </el-button>
        </el-form-item>
      </el-form>
      <!-- 角色数据表单 -->
      <el-table ref="multipleTable" :data="dataList" border>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="项目编号" prop="projectId" width="80">
        </el-table-column>
        <el-table-column label="项目名称" prop="project.name" width="150">
        </el-table-column>
        <el-table-column label="项目类型" prop="project.type" width="130">
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
              查看项目
            </el-button>
            <el-button @click="openEditWindow(scope.row)" size="mini" type="primary">
              修改项目
            </el-button>
            <el-button @click="deleteProject(scope.row)" size="mini" type="danger">
              删除项目
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
        <el-descriptions>
          <el-descriptions-item :span="1" label="创建时间">
            {{this.projectData.createTime}}
          </el-descriptions-item>
          <el-descriptions-item :span="1" label="最近一次修改时间" prop="updateTime">
            {{ this.projectData.updateTime }}
          </el-descriptions-item>
        </el-descriptions>


        <el-descriptions>
          <el-descriptions-item :span="1" label="附件">
            <div id="files">
              <ul style="margin-left: -5%">
                <li v-for="item in this.filesJSON" :key="item.id" style="list-style:none;float: left">
                  <el-button @click="downloadFile(item.id)">
                    {{ item.name }}
                  </el-button>
                </li>
                <li v-if="this.filesJSON===null" style="list-style:none;float: left;margin-top: -10%"><span>空</span>
                </li>
              </ul>
            </div>
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
              {{this.userData.status === 1 ? "激活" : "禁用"}}
          </el-descriptions-item>
        </el-descriptions>
      </el-dialog>
      <!-- 新增按钮打开的页面 -->
      <el-dialog title="创建项目" :visible.sync="dialogFormVisible">
        <el-form :model="insertData">
          <el-form-item label="项目名称" label-width="120px">
            <el-input
              v-model="insertData.name"
              placeholder="项目名称"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="项目内容" label-width="120px">
            <el-input
              v-model="insertData.content"
              placeholder="项目内容"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="项目类型" label-width="120px">
            <el-input
              v-model="insertData.type"
              placeholder="项目类型"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="申请资金" label-width="120px">
            <el-input
              v-model="insertData['money']"
              placeholder="申请资金"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="项目说明" label-width="120px">
            <el-input
              v-model="insertData['remark']"
              style="width: 80%"
              type="textarea"
            />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="checkItemBeforeInsert()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 编辑按钮打开的页面 -->
      <el-dialog title="编辑项目内容" :visible.sync="editWindow">
        <el-form :model="editData">
          <el-form-item label="项目名称" label-width="120px">
            <el-input
              v-model="editData.name"
              placeholder="账号"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="项目内容" label-width="120px">
            <el-input
              v-model="editData.content"
              placeholder="项目内容"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="项目类型" label-width="120px">
            <el-input
              v-model="editData.type"
              placeholder="项目类型"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="申请资金" label-width="120px">
            <el-input
              v-model="editData['money']"
              placeholder="申请资金"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="项目说明" label-width="120px">
            <el-input
              v-model="editData['remark']"
              style="width: 80%"
              type="textarea"
            />
          </el-form-item>
          <el-form-item label="上传附件" label-width="120px">
            <el-button @click="changeDialogFormVisibleFile()">上传附件</el-button>
            <el-upload
              v-if="this.fileWindow"
              :auto-upload="false"
              :before-upload="handleBeforeUpload"
              :file-list="fileList"
              :limit="fileLimit"
              :multiple="true"
              :on-change="handleChange"
              :on-exceed="handleExceed"
              :show-file-list="true"
              action="#"
              drag
              list-type="text"
              name="file"
              style="margin-top: 10px;"
            >
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div slot="tip" class="el-upload__tip">只能上传doc/xls/ppt文件，且不超过{{ this.fileSize }}kb</div>
            </el-upload>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editWindow = false">取 消</el-button>
          <el-button type="primary" @click="updateProject()">更 新</el-button>
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
  name: "MyProject",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      editData: {}, //编辑数据
      insertData: {},//新增数据
      projectData: {},//查看数据
      userData: {},//创建者数据

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
      dialogFormVisible: false, //默认关闭新建用户界面
      editWindow: false, //默认关闭编辑用户界面
      lookWindow: false,
      fileWindow: false,//上传文件

      //文件参数
      fileList: [], //上传文件，以数组方式上传
      fileListed: 0, //上传过的文件
      fileLimit: 5, //文件上传个数限制
      fileSize: 10,//文件限制大小
      filesJSON: [],//回显附件路径
      returnList: [],//返回 文件上传的本地路径

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

    //下载附件
    downloadFile(id) {
      this.$http.post("/file/download/" + id).then((res) => {
        this.$DownloadFile(res["name"] + '.' + res["type"], res["stream"])
      });
    },
    //是否显示上传框
    changeDialogFormVisibleFile() {
      this.fileWindow = !this.fileWindow
    },
    //上传前的校验
    handleBeforeUpload(file) {
      const uploadTypes = [
        "doc",
        "docx",
        "xls",
        "xlsx",
        "ppt",
        "pptx",
      ];
      const filetype = file.name.replace(/.+\./, "");
      const isRightSize = (file.size || 0) / 1024 / 1024 < this.fileSize;
      if (!isRightSize) {
        message.error("文件大小超过 " + this.fileSize + "MB");
        return false;
      }
      if (uploadTypes.indexOf(filetype.toLowerCase()) === -1) {
        message.warning("请上传后缀名为doc,docx,xls,xlsx,ppt,pptx的附件");
        return false;
      }
      return true;
    },
    //文件数量改变
    handleChange(fileList) {
      this.fileList[this.fileListed] = fileList;
      this.fileListed = this.fileListed + 1
    },
    //超出文件个数的回调
    handleExceed() {
      message({
        type: "warning",
        message: "超出最大上传文件数量的限制！",
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
          message({
            type: "info",
            message: "已取消创建项目",
          });
        });
      } else {
        this.insertProject()
      }
    },
    //修改项目
    updateProject() {
      this.$confirm("是否确认修改?修改后项目将回到未审核状态！", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        let list = this.fileList;
        if (list.length !== 0) {
          this.$GlobalLoading(true,{text: '正在上传文件中，请稍后...',})
          this.$UploadFile("/import/file", list).then(() => {
            setTimeout(()=>{
              this.$GlobalLoading(false,null);
            },1000)
          })
        }
        this.editWindow = false;
        const data = {
          projectBo: this.editData,
          pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
        }
        this.$http.post("/item/update/myItem", data).then((res) => {
          this.cutDataList(res);
        });
      });
    },
    //删除按钮
    deleteProject(item) {
      this.$confirm("是否确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          const data = {
            itemId: item.id,
            projectId: item.projectId,
            pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
          }
          this.$http.post("/item/delete/myItem", data).then((res) => {
            this.cutDataList(res);
          });
        })
        .catch(() => {
          message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    //查找项目的附件
    searchFiles(val) {
      this.$http.post("/file/getFileList/" + val).then((res) => {
        this.filesJSON = res
      });
    },


    //查询所有数据
    getDataList() {
      this.dataListFrom = "getDataList";
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/item/getPageVo/MyItem", pageInfo).then((res) => {
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
      this.$http.post("/item/getPageVo/MyItem/search", data).then((res) => {
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
      //查找项目的附件
      this.searchFiles(item.project.id)
      this.projectData = item.project;
      this.userData = item.user;
      this.projectData.status = item.status
      this.projectData.createTime = item.createTime
      this.projectData.updateTime = item.updateTime
    },
    //编辑按钮
    openEditWindow(item) {
      this.editWindow = true; //打开编辑界面
      //回显数据
      this.editData = this.$CopyObject(item.project);
      this.editData.id = item.id;
      //清空上传列表
      this.fileList = [];
      this.fileListed = 0
      this.fileWindow = false;//上传文件
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

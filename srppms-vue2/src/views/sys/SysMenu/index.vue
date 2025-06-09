<template>
  <el-card class="box-card">
    <div>
      <!-- 搜索框 -->
      <el-form v-show="showSearch" ref="searchData" :inline="true" :model="searchData" class="demo-form-inline" size="small">
        <el-form-item label="名称" style="width: 20%">
          <el-input
            v-model="searchData.name"
            clearable
            placeholder="菜单名称"
          />
        </el-form-item>
        <el-form-item label="URL" style="width: 20%">
          <el-input
            v-model="searchData.url"
            clearable
            placeholder="菜单URL"
          />
        </el-form-item>
        <el-form-item label="父菜单" style="width: 22%">
          <el-select v-model="searchData.parentId" clearable placeholder="请选择父菜单">
            <el-option
              v-for="{id,name} in parentMenus"
              :key="id"
              :label="name"
              :value="id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="类型" style="width: 20%">
          <el-select v-model="searchData.type" clearable placeholder="请选择菜单类型">
            <el-option
              v-for="{type,name} in types"
              :key="type"
              :label="name"
              :value="type"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="getDataListBySearch">查询</el-button>
          <el-button icon="el-icon-refresh" type="success" @click="refresh">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5">
          <el-button v-button="':SysMenu:insert'" plain size="mini" type="primary" @click="openInsertWindow">
            新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysMenu:update'" plain size="mini" type="success" @click="openEditWindow" :disabled="single">
            修改
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button v-button="':SysMenu:delete'" plain size="mini" type="danger" @click="deleteMenu" :disabled="multiple">
            删除
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <printTable v-button="':SysMenu:print'" :data="getPrintData" :properties="properties"/>
        </el-col>

        <right-toolbar :columns="properties" :showSearch.sync="showSearch" @queryTable="getDataList"></right-toolbar>
      </el-row>

      <!-- 角色数据表单 -->
      <el-table :data="dataList" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" class="table-wrap"
                border row-key="id" @selection-change="handleSelectionChange">
	      <el-table-column align="center" type="selection"/>
        <el-table-column
	        v-for="{prop, label, width} in getProp"
	        :key="prop"
	        :label="label"
	        :prop="prop"
	        :width="width"
	        sortable>
          <!-- 图标 -->
          <template v-if="prop==='icon'" v-slot="scope">
            <i :class="`el-icon-${scope.row.icon}`"></i>
          </template>
          <!-- 类型 -->
          <template v-else-if="prop==='type'" v-slot="scope">
            <div v-for="{type,name} in types" :key="type">
              <span v-if="type === scope.row.type ">{{ name }}</span>
            </div>
          </template>
          <!-- 权限 -->
          <template v-else-if="prop==='access'" v-slot="scope">
            <div v-for="{id,roleName,access} in roles" :key="id">
              <span v-if="access === scope.row['access'] ">{{ roleName }}</span>
            </div>
          </template>
        </el-table-column>
	      <el-table-column label="操作" min-width="200">
          <template v-slot="scope">
            <el-button v-button="':SysMenu:update'" size="mini" type="primary" @click="openEditWindow(null,scope.row)">
              修改
            </el-button>
            <el-button v-button="':SysMenu:delete'" size="mini" type="danger" @click="deleteMenu(null,scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 新增按钮打开的页面 -->
      <el-dialog :visible.sync="lookWindow" title="新增菜单">
        <el-form :model="insertData">
          <el-form-item label="菜单名称" label-width="20%">
            <el-input
              v-model="insertData.name"
              placeholder="菜单名称"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="菜单URL" label-width="20%">
            <el-input
              v-model="insertData.url"
              placeholder="菜单URL"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="菜单类型" label-width="20%">
            <el-select v-model="insertData.type" placeholder="请选择">
              <el-option
                v-for="{type,name} in types"
                :key="type"
                :label="name"
                :value="type"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="菜单图标" label-width="20%">
            <el-select v-model="insertData.icon" clearable filterable placeholder="请选择">
              <el-row style="width: 100%;">
                <el-col v-for="item in icons" :key="item.icon" :span="6">
                  <el-option v-for="{icon,name} in item" :key="icon" :label="name" :value="icon" class="el-option">
                    <i :class="`el-icon-${icon}`"/>
                  </el-option>
                </el-col>
              </el-row>
            </el-select>
          </el-form-item>
          <el-form-item label="排序" label-width="20%">
            <el-input
              v-model="insertData['orderNum']"
              placeholder="默认为 1 "
              style="width: 80%"
              type="number"
            />
          </el-form-item>
          <el-form-item label="父菜单" label-width="20%">
            <el-select v-model="insertData.parentId" clearable filterable placeholder="默认无父菜单">
              <el-option
                v-for="{id,name} in getParentMenus"
                :key="id"
                :label="name"
                :value="id"/>
            </el-select>
          </el-form-item>
          <el-form-item label="访问权限" label-width="20%">
            <el-select v-model="insertData['access']" placeholder="访问菜单需要的权限">
              <el-option
                v-for="{id,roleName,access} in roles"
                :key="id"
                :label="roleName"
                :value="access"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="是否显示" label-width="20%">
            <el-select v-model="insertData['isShow']" placeholder="是否显示">
              <el-option label="显示" value="显示"></el-option>
              <el-option label="不显示" value="不显示"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="lookWindow = false;">取 消</el-button>
          <el-button type="primary" @click="insertMenu()">确 定</el-button>
        </div>
      </el-dialog>
      <!-- 修改按钮打开的页面 -->
      <el-dialog :visible.sync="editWindow" title="修改菜单">
        <el-form :model="editData">
          <el-form-item label="菜单名称" label-width="20%">
            <el-input
              v-model="editData.name"
              placeholder="菜单名称"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="菜单URL" label-width="20%">
            <el-input
              v-model="editData.url"
              placeholder="菜单URL"
              style="width: 80%"
            />
          </el-form-item>
          <el-form-item label="类型" label-width="20%">
            <el-select v-model="editData.type" placeholder="">
              <el-option
                v-for="{type,name} in types"
                :key="type"
                :label="name"
                :value="type"/>
            </el-select>
          </el-form-item>
          <el-form-item label="菜单图标" label-width="20%">
            <el-select v-model="editData.icon" clearable filterable placeholder="请选择">
              <el-row>
                <el-col v-for="item in icons" :key="item.icon" :span="6">
                  <el-option v-for="{icon,name} in item" :key="icon" :label="name" :value="icon" class="el-option">
                    <i :class="`el-icon-${icon}`"/>
                  </el-option>
                </el-col>
              </el-row>
            </el-select>
          </el-form-item>
          <el-form-item label="排序" label-width="20%">
            <el-input
              v-model="editData['orderNum']"
              placeholder="排序"
              style="width: 80%"
              type="number"
            />
          </el-form-item>
          <el-form-item v-if="editData.type !== 0" label="父菜单" label-width="20%">
            <el-select placeholder="请选择父菜单" clearable filterable v-model="editData.parentId">
              <el-option
                v-for="{id,name} in getParentMenus"
                :key="id"
                :label="name"
                :value="id"/>
            </el-select>
          </el-form-item>
          <el-form-item label="访问权限" label-width="20%">
            <el-select v-model="editData['access']">
              <el-option
                v-for="{id,roleName,access} in roles"
                :key="id"
                :label="roleName"
                :value="access"/>
            </el-select>
          </el-form-item>
          <el-form-item label="是否显示" label-width="20%">
            <el-select v-model="editData['isShow']">
              <el-option label="显示" value="显示"></el-option>
              <el-option label="不显示" value="不显示"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editWindow = false">取 消</el-button>
          <el-button type="primary" @click="updateMenu()">更 新</el-button>
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
import {getRouter} from "@/router";
import {message} from "@/method/message";
import cache from "@/method/cache";

export default {
  name: "SysMenu",
  data() {
    return {
      //数据参数
      searchData: {},//搜索数据
      editData: {}, //修改数据
      insertData: {},//新增数据

      //集合参数
      icons: [], //添加是可以选择的图标
      dataList: [], //页面展示的数据集合
      parentMenus: [], //父级菜单
      types: [], //菜单类型
      roles: [], //角色信息菜单
      selections: [],//多选数组

      //页号参数
      pageIndex: 1,
      pageSizes: [],
      pageSize: 5,
      totalPage: 0,
      begin: 0,
      end: this.pageSize - 1,

      //窗口显示参数
      lookWindow: false, //默认关闭新建菜单界面
      editWindow: false, //默认关闭修改菜单界面

      //其他
      single: true,//非单个禁用
      multiple: true,//非多个禁用
      showSearch: true,// 显示搜索条件
      dataListFrom: "getDataList",//当前数据来源于搜索还是全局

      //表头数据
      properties: [],
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

    //打开新增页面
    openInsertWindow() {
      //刷新表格
      this.insertData = {};
      //清空父级菜单
      this.parentMenus = [];
      //获取父菜单
      this.getParentMenu();
      //打开页面
      this.lookWindow = true;
    },
    //打开修改页面
    openEditWindow(item, data) {
      //判断时多选框中选择，还是单条点击
      data = item === null ? data : this.selections[0];
      //打开修改菜单界面
      this.editWindow = true;
      //清空回显数据
      this.editData = [];
      //回显数据
      this.editData = this.$CopyObject(data);
      //清空父级菜单
      this.parentMenus = [];
      //获取父菜单
      this.getParentMenu();
    },

    //增加菜单
    insertMenu() {
      const data = {
        menuBo: this.$CopyObject(this.insertData),
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/menu/insert", data).then((res) => {
        //关闭窗口
        this.lookWindow = false;
        //分页操作
        this.cutDataList(res);
        //更新路由
        getRouter()
      });
    },
    //修改菜单
    updateMenu() {
      const data = {
        menuBo: this.editData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/menu/update", data).then((res) => {
        //关闭窗口
        this.editWindow = false;
        //分页操作
        this.cutDataList(res)
        //更新路由
        getRouter()
      });
    },
    //删除菜单
    deleteMenu(item,data) {
      const list = []
      //判断时多选框中选择，还是单条点击
      if(item === null) {
        list.push(data.id)
      }else {
        this.selections.forEach(item => list.push(item.id))
      }
      this.$confirm("是否确认删除?", "提示", {confirmButtonText: "确定",cancelButtonText: "取消",type: "warning"})
        .then(() => {
          const data = {
            list: list,
            pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
          }
          this.$http.post("/menu/delete", data).then((res) => {
            //分页操作
            this.cutDataList(res)
            //更新路由
            getRouter()
          });
        })
        .catch(() => {
          message.info("已取消删除");
        });
    },
    //刷新页面
    refresh() {
      this.$GlobalLoading(true, {text: "正在重置搜索条件，请稍候..."});
      setTimeout(() => {
        this.searchData = {};
        this.getDataList();
        this.$GlobalLoading(false, null);
      }, 1000);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selections = selection;
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    //查询所有数据
    getDataList() {
      this.dataListFrom = "getDataList";
      const pageInfo = this.$getPageInfo(this.pageSize, this.pageIndex)
      this.$http.post("/menu/getPageVo", pageInfo).then((res) => {
        this.cutDataList(res);
      });
    },
    //查询单条数据
    getDataListBySearch() {
      this.dataListFrom = "getDataListBySearch";
      const data = {
        menuBo: this.searchData,
        pageBo: this.$getPageInfo(this.pageSize, this.pageIndex)
      }
      this.$http.post("/menu/getPageVo/search", data).then((res) => {
        this.cutDataList(res);
      })
    },
    //获取父菜单
    getParentMenu() {
      this.$http.post("/menu/getList").then((res) => {
        this.parentMenus = res;
      });
    },
    //获取角色信息
    getRoleList() {
	    const key = ":Role:List";
	    const list = cache.session.get(key);
	    if (list) {
		    this.roles = JSON.parse(list)
	    } else {
		    this.$http.post("/role/getList/roleKind").then((res) => {
			    this.roles = res;
			    cache.session.set(key, JSON.stringify(this.roles));
		    });
	    }
    },
    //获取菜单类型字典
    getType() {
	    const key = ":Menu:Types";
	    const list = cache.session.get(key);
	    if (list) {
		    this.types = JSON.parse(list)
	    } else {
		    this.$http.post("/dict/getDictVoList/menuType").then(res => {
			    this.types = this.$StrToJson(res);
			    cache.session.set(key, JSON.stringify(this.types));
		    })
	    }
    },
    //获取菜单图标字典
    getIcon() {
	    const key = ":Menu:Icons";
	    const list = cache.session.get(key);
	    if (list) {
		    this.icons = JSON.parse(list)
	    } else {
		    this.$http.post("/dict/getDictVoList/icon").then(res => {
			    this.icons = this.$StrToJson(res);
			    const len = this.icons.length //数组长度
			    const num = 4 //数组数量
			    const arr = []
			    for (let i = 0; i < len; i++) {
				    if (i < num) {
					    arr.push([])
				    }
				    arr[i % num].push(this.icons[i])
			    }
			    this.icons = arr
			    cache.session.set(key, JSON.stringify(this.icons));
		    })
	    }
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
    },

    //获取表头
    getProperties() {
      this.properties = this.$getProp("/prop/menu")
    }
  },
  mounted() {
    //获取表头数据
    this.getProperties();
    //获取页面尺寸
    this.setPageSize();
    //获取所有菜单
    this.getDataList();
    //获取父菜单
    this.getParentMenu();
    //导入icon图标
    this.getIcon();
    //导入types类型
    this.getType();
    //获取权限列表
    this.getRoleList();
  },
  computed: {
    //获取打印集合
    getPrintData() {
      let data = []
      if (this.selections.length === 0) {
        data = JSON.parse(JSON.stringify(this.dataList));
      } else {
        data = JSON.parse(JSON.stringify(this.selections));
      }
      return data;
    },
    //获取父菜单
    getParentMenus() {
      const menu = []
      this.parentMenus.forEach(item => {
        menu.push(item)
      })
      if (this.insertData.type === 2 || this.editData.type === 2) {
        this.parentMenus
          .filter(item => item.children !== null)
          .map(item => item.children)
          .forEach(item1 => {
            for (let k in item1) {
              menu.push(item1[k])
            }
          })
      }
      menu.push({id: 0, name: "无目录"})
      return menu
		},
		//获取表头
		getProp() {
			return this.properties.filter(item => item.checked);
		}
  }
};
</script>
<style lang="less">
</style>

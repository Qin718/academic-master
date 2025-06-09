<template>
  <div>
    <el-dropdown :hide-on-click="false" style="margin-right: 10px">
    <span class="el-dropdown-link">
      <img id="picture" alt="" class="user_img" v-bind:src="userInfo.image"/>
    </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="PersonalData">个人信息</el-dropdown-item>
        <el-dropdown-item @click.native="setting = true">布局设置</el-dropdown-item>
        <el-dropdown-item divided @click.native="loginOut">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>
<script>
//头部右侧头像
import {mapState} from "vuex";
import cache from "@/method/cache";

export default {
  name: "user",
  data() {
    return {};
  },
  methods: {
    //个人中心
    PersonalData() {
      this.$router.push("/PersonalData");
    },
    //退出
    loginOut() {
      this.$confirm("是否确认退出?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$http.post("/login/out")
        // 表示处理的是 注销操作
        // 清空存储的Token信息
	      cache.session.removeAll();
        //清除自动登录
	      cache.local.remove("auto");
        // 然后跳转到登录页
        this.$router.push("/");
      });
    },
    //回显个人信息
    getData() {
      this.$http.post("/user/getInfo").then((res) => {
        //图片转码
        res["image"] = "data:image/png;base64," + this.$unzip(res["image"]);
        this.$store.dispatch('userInfo/setUserInfo', res)
      });
    },
  },
  mounted() {
    this.getData();
  },
  computed: {
    ...mapState(["userInfo"]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
  },
};
</script>
<style lang="less">
</style>

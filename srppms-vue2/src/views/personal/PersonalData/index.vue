<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div style="text-align: center">
              <userImage/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <i :class="`el-icon-user`"/> 用户名称
                <div class="pull-right">{{ userInfo.username }}</div>
              </li>
              <li class="list-group-item">
                <i :class="`el-icon-user-solid`"/> 所属角色
                <div class="pull-right">{{ userInfo.roleName }}</div>
              </li>
              <li class="list-group-item">
                <i :class="`el-icon-phone`"/> 手机号码
                <div class="pull-right">{{ userInfo.mobile }}</div>
              </li>
              <li class="list-group-item">
                <i :class="`el-icon-message`"/> 用户邮箱
                <div class="pull-right">{{ userInfo.email }}</div>
              </li>
              <li class="list-group-item">
                <i :class="`el-icon-date`"/> 创建日期
                <div class="pull-right">{{ userInfo.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userImage from "../userImage";
import userInfo from "../userInfo";
import resetPwd from "../resetPwd";

export default {
  name: "PersonalData",
  components: {userImage, userInfo, resetPwd},
  data() {
    return {
      activeTab: "userinfo"
    };
  },
  computed: {
    userInfo() {
      return this.$store.state.userInfo;
    }
  }
};
</script>
<style lang="less">
.list-group {
  padding-left: 0;
  list-style: none;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0;
  font-size: 13px;
}

.list-group-striped > .list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.pull-right {
  float: right !important;
}
</style>

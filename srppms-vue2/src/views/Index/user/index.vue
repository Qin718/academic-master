<template>
  <div>
    <el-card class="box-card">
      <div class="user">
        <img id="picture" :src="userInfo.image" alt=""/>
        <div class="userinfo">
          <p id="username" class="name">{{ userInfo.username }}</p>
          <p id="roleName" class="access">{{ userInfo.roleName }}</p>
        </div>
      </div>

    </el-card>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: "userImage",
  computed: {
    ...mapState(["userInfo"]),
  }
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

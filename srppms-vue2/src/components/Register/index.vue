<template>
  <div class="login_container">
    <div class="login_form">
      <p class="login_title">注册页面</p>
      <el-form
        :model="insertData"
        :rules="rules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="insertData.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="insertData.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="insertData.password"
            placeholder="请输入密码"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="重复密码" prop="passwords">
          <el-input
            v-model="insertData.passwords"
            placeholder="请输入密码"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号码" prop="mobile">
          <el-input v-model="insertData.mobile" placeholder="请输入手机号码"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="insertData.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <div align="center">
          <el-button size="medium" type="primary" @click="register">
            注册
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script>
import {message} from "@/method/message";
import cache from "@/method/cache";

export default {
  name: "register",
  data() {
    return {
      insertData: {
        name: "",
        username: "",
        password: "",
        passwords: "",
        mobile: "",
        email: ""
      },
      rules: {
        name: [{required: true, message: "请输入姓名", trigger: "blur"}],
        username: [{required: true, message: "请输入账号", trigger: "blur"}],
        password: [{required: true, message: "请输入密码", trigger: "blur"}],
        passwords: [{required: true, message: "请输入密码", trigger: "blur"}],
      },
    };
  },
  methods: {
    //注册
    register() {
      if (this.insertData.password !== this.insertData.passwords) {
        //两次密码不一样
        message.info("两次密码不一致");
      } else {
        this.$http.post("/login/register", this.insertData).then((res) => {
	        cache.session.set("token", res["token"]);
	        cache.session.set("username", res["username"]);
	        cache.session.set("tokenName", res["tokenName"]);
	        cache.session.set("account", res["account"]);
          this.$router.push("/Index");
        });
      }
    },
  },
};
</script>
<style lang="less" scoped>
.login_container {
  width: 100%;
  height: 150vh;
  background-color: rgba(242, 242, 242, 1);
  background-image: require("@/assets/images/login.jpg");
  background-position: center center;
  background-repeat: no-repeat;
  background-attachment: scroll;
  background-size: 1920px 950px;
  border: none;
  border-radius: 0;
  display: flex;
  justify-content: center;
  align-items: center;

  .login_form {
    width: 510px;
    margin: 0 auto;
    padding: 0 55px 15px 35px;
    background-color: #fff;
    border: none;
    border-radius: 5px;
    box-shadow: 0 0 25px #cac6c6;

    .login_title {
      font-family: "微软雅黑 Bold", "微软雅黑", serif;
      font-weight: 700;
      text-decoration: none;
      color: rgb(0, 121, 254);
      font-size: 32px;
      margin-top: 50px;
      margin-bottom: 30px;
      text-align: center;
    }
  }
}
</style>

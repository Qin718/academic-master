<template>
  <div class="login_container">
    <div class="login_form">
      <p class="login_title">登录页面</p>
      <el-form
        ref="formName"
        :model="loginData"
        :rules="rules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="账号" prop="account">
          <el-input v-model="loginData.account" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginData.password"
            placeholder="请输入密码"
            type="password"
          ></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-input
            v-model="loginData.code"
            auto-complete="false"
            placeholder="点击图片更换验证码"
            style="width: 60%; margin-left: 10px"
            type="text"
          ></el-input>
          <el-image
            :src="this.image"
            class="codeImg"
            @click="resetImg"
          ></el-image>
        </el-form-item>
        <div style="margin-left: 20%">
          <el-checkbox v-model="remember" class="remember">记住密码</el-checkbox>
          <el-checkbox v-model="auto" class="auto">自动登录</el-checkbox>
        </div>
        <div style="margin-top: 3%;text-align: center;">
          <el-button v-if="!isLogin" size="medium" type="primary" @click="checkCode">登录</el-button>
          <el-button v-if="!isLogin" size="medium" type="primary" @click="register">注册</el-button>
          <el-button v-if="isLogin" disabled size="medium" type="primary">登录中</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import {getRouter} from "@/router";
import {message} from "@/method/message";
import cache from "@/method/cache";

const Base64 = require('js-base64').Base64

export default {
  name: "Login",
  data() {
    return {
      image: "", //二维码图片
      code: "", //二维码文字
      loginData: {
        account: "",
        password: "",
        code: "",
      },
      rules: {
        account: [{required: true, message: "请输入账号", trigger: "blur"}],
        password: [{required: true, message: "请输入密码", trigger: "blur"}],
        code: [{required: true, message: "请输入验证码", trigger: "blur"}],
      },
      remember: false,
      auto: false,
      isLogin: false,
      codeTimer: null,
    };
  },
  methods: {
      //检验验证码
    checkCode() {
      this.isLogin = true;
      if (this.loginData.code === "") {
        message.info("请输入验证码");
        this.resetImg();
      } else if (this.loginData.code !== this.code) {
        message.info("验证码错误");
        this.resetImg();
      } else {
        this.login()
      }
    },
    //登录
    login() {
      this.loginData.auto = this.auto
      this.loginData.remember = this.remember
      this.$http.post("/login/", this.loginData).then((res) => {
        if (res !== undefined && res !== null) {
          getRouter();
          res["account"] = this.loginData.account
          this.keepStorage(res);
          this.keepAccount();
          this.$router.push("/Index");
        } else {
          //获取验证码
          this.resetImg();
        }
      });
    },
    //注册
    register() {
      this.$router.push("/register");
    },
    //生成验证码
    resetImg() {
      clearTimeout(this.codeTimer);
      this.$http.post("/code/").then((res) => {
        this.isLogin = false;
        if (res === undefined || res === null) {
          message.info("获取验证码失败，60秒后将重新加载。")
          this.codeTimer = setTimeout(() => {
            this.resetImg()
          }, 60 * 1000)
        } else {
          this.code = res["code"];
          const image = this.$unzip(res["image"]);
          console.log("验证码：" + this.code);
          this.image = "data:image/png;base64," + image;
          //同步后端验证码过期时间，重新获取验证码。
          this.codeTimer = setTimeout(() => {
            this.resetImg()
          }, 5 * 60 * 1000)
        }
      });
    },
    //获取客户端信息
    getClientInformation(){
      //ip地址
	    cache.session.set("Ip", this.$getIp())
      //浏览器类型
	    cache.session.set("Browser", this.$getBrowser())
      //系统类型
	    cache.session.set("OS", this.$getOS())
      //屏幕分辨率
	    // cache.session.set("Resolution",this.$getResolution())
      //颜色深度
	    // cache.session.set("colorDepth",this.$getColorDepth())
    },
    //保存账号密码
    keepAccount() {
      if (this.remember) {
        //账号
	      cache.local.set('account', this.loginData.account)
        //密码 加密
        let password = Base64.encode(this.loginData.password) // base64加密
	      cache.local.set('password', password)
        //记住我
	      cache.local.set("remember", this.remember);
        //自动登录
	      cache.local.set("auto", this.auto);
      } else {
	      cache.local.remove('account')
	      cache.local.remove('password')
	      cache.local.remove('remember')
	      cache.local.remove('auto')
      }
    },
    //保存当前用户的缓存
    keepStorage(res) {
	    cache.session.set("token", res["token"]);
	    cache.session.set("username", res["username"]);
	    cache.session.set("tokenName", res["tokenName"]);
	    cache.session.set("account", res["account"]);
    }
  },
  
  mounted() {
    // this.resetImg();//获取验证码
    this.getClientInformation();
  },
  created() {
      //虚拟登录
    if (this.$route.query["token"] !== undefined) {
	    cache.session.removeAll()
      const res = this.$route.query
      this.keepStorage(res);
      this.$router.push("/Index");
    } else {
      //记住密码-回显账号密码
	    this.remember = Boolean(cache.local.get('remember'))
      if (this.remember) {
	      this.loginData.account = cache.local.get('account')
	      this.loginData.password = Base64.decode(cache.local.get('password'))
	      this.auto = Boolean(cache.local.get('auto'))
        if (this.auto) {
          this.login();
        } else {
          this.resetImg();//获取验证码
        }
      }
      //验证码登录
      else {
        //获取验证码
        this.resetImg();
      }
    }
  },
  watch: {
    remember: function (val) {
      if (val === false) {
        this.auto = false;
      }
    },
    auto: function (val) {
      if (val === true) {
        this.remember = true;
      }
    },
  },
  beforeDestroy() {
    clearTimeout(this.codeTimer);
  }
}
</script>

<style lang="less" scoped>
.codeImg {
  margin-top: 5px;
  float: right;
}

.login_container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;

  // 全屏背景图设置
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("~@/assets/images/login.jpg");
    background-position: center center;
    background-repeat: no-repeat;
    background-size: cover;
    background-attachment: fixed;
    z-index: -1;
    filter: blur(0px) brightness(0.7);
  }

  // 深色半透明覆盖层，增强文本可读性
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0);
    z-index: -1;
  }

  .login_form {
    width: 510px;
    margin: 0 auto;
    padding: 30px 55px 35px;
    background: rgba(255, 255, 255, 0.95);
    border: none;
    border-radius: 8px;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
    position: relative;
    z-index: 1;
    transform: translateY(-20px);
    transition: transform 0.4s ease, box-shadow 0.4s ease;

    &:hover {
      transform: translateY(-15px);
      box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
    }

    .login_title {
      font-family: "微软雅黑 Bold", "微软雅黑", serif;
      font-weight: 700;
      text-decoration: none;
      color: #0079fe;
      font-size: 32px;
      margin: 10px 0 25px;
      text-align: center;
      position: relative;
      
      &::after {
        content: '';
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
        width: 50px;
        height: 3px;
        background: linear-gradient(90deg, #0079fe, #00d1ff);
        border-radius: 3px;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .login_container {
    padding: 20px;
    align-items: flex-start;
    padding-top: 60px;
    
    .login_form {
      width: 100%;
      max-width: 500px;
      padding: 20px 30px;
    }
  }
}

@media (max-width: 480px) {
  .login_container {
    .login_form {
      padding: 20px 15px;
      
      .login_title {
        font-size: 28px;
      }
    }
    
    ::v-deep .el-form-item__label {
      width: 70px !important;
    }
    
    ::v-deep .el-input {
      width: calc(100% - 80px);
    }
  }
}
</style>
<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="旧密码" prop="oldPassword">
      <el-input v-model="user.oldPassword" placeholder="请输入旧密码" show-password type="password"/>
    </el-form-item>
    <el-form-item label="新密码" prop="password">
      <el-input v-model="user.password" placeholder="请输入新密码" show-password type="password"/>
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input v-model="user.confirmPassword" placeholder="请确认新密码" show-password type="password"/>
    </el-form-item>
    <el-form-item>
      <el-button size="mini" type="primary" @click="updatePwd">保存</el-button>
      <el-button size="mini" type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import cache from "@/method/cache";

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      user: {
        oldPassword: undefined,
        password: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [
          {required: true, message: "旧密码不能为空", trigger: "blur"}
        ],
        password: [
          {required: true, message: "新密码不能为空", trigger: "blur"},
          {min: 6, max: 20, message: "长度在 6 到 20 个字符", trigger: "blur"},
          {pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\ |", trigger: "blur"}
        ],
        confirmPassword: [
          {required: true, message: "确认密码不能为空", trigger: "blur"},
          {required: true, validator: equalToPassword, trigger: "blur"}
        ]
      }
    };
  },
  methods: {
    updatePwd() {
      this.$http.post("/user/updatePwd", this.user).then(() => {
        this.$GlobalLoading(true, {text: "正在跳转登录页面，请稍候..."});
        setTimeout(() => {
          // 清空存储的Token信息
	        cache.session.removeAll();
          // 清除记住密码
	        cache.local.remove("remember");
          // 清除自动登录
	        cache.local.remove("auto");
          //关闭loading
          this.$GlobalLoading(false, null);
          // 然后跳转到登录页
          this.$router.push("/login");
        }, 1000)
      });
    },
    close() {
			this.$router.push('/Index');
    }
  }
};
</script>

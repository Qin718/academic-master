<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="username">
      <el-input v-model="form.username" maxlength="30"/>
    </el-form-item>
    <el-form-item label="手机号码" prop="mobile">
      <el-input v-model="form.mobile" maxlength="11"/>
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" maxlength="50"/>
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.sex">
        <el-radio label="男">男</el-radio>
        <el-radio label="女">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button size="mini" type="primary" @click="submit">保存</el-button>
      <el-button size="mini" type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  data() {
    return {
      form: {},
      // 表单校验
      rules: {
        username: [
          {required: true, message: "用户昵称不能为空", trigger: "blur"}
        ],
        email: [
          {required: true, message: "邮箱地址不能为空", trigger: "blur"},
          {type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"]}
        ],
        mobile: [
          {required: true, message: "手机号码不能为空", trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur"}
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$http.post("/user/updateUserInfo", this.form);
      this.$GlobalLoading(true, {text: "正在更新个人信息，请稍候..."});
      setTimeout(() => {
        let userInfo = this.$store.state.userInfo;
        for (const i in this.form) {
          userInfo[i] = this.form[i]
        }
        this.$store.dispatch('userInfo/setUserInfo', userInfo);
        this.$GlobalLoading(false, null);
      }, 1000)
    },
    close() {
      this.$router.push('/Index');
    },
    getUserInfo() {
      this.form = this.$CopyObject(this.$store.state.userInfo);
    },
  },
  mounted() {
    this.getUserInfo();
  },
};
</script>

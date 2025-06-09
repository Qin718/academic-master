<template>
  <div>
    <el-card class="box-card" style="margin-top: 20px">
      <el-table :data="LoginData" stripe>
        <el-table-column label="日期" prop="time" width="180">
        </el-table-column>
        <el-table-column label="姓名" prop="username" width="70">
        </el-table-column>
        <el-table-column label="地址" prop="ip"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "login",
  data() {
    return {
      LoginData: [],
    };
  },
  methods: {
    getLogin() {
      this.$http.post("/login/getList/Index").then((res) => {
        this.LoginData = res;
      });
    },
  },
  mounted() {
    this.getLogin();
  },
};
</script>

<style lang="less" scoped>

</style>

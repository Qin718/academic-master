<template>
  <div>
    <el-row>
      <el-col :span="8">
        <user/>
        <login/>
      </el-col>
      <el-col :span="16" style="padding-left: 20px ;height: 100px;">
				<update/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import user from "./user"
import login from "./login"
import update from "./update";

export default {
  name: "index",
	components: {update, user, login},
};
</script>

<style lang="less" scoped>

</style>

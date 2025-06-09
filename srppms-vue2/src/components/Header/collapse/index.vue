<template>
  <el-button
    icon="el-icon-menu"
    size="medium"
    style="margin-left: 20px"
    @click="handleMenu"
  ></el-button>
</template>
<script>

//左侧菜单缩放按键
import store from "@/store";

export default {
  name: "collapse",
  methods: {
    //对菜单进行缩放操作
    handleMenu() {
      store.dispatch('app/changeCollapse')
    },
  },
};
</script>
<style lang="less">
</style>

<template>
  <div>
    <template v-for="item in menu">
      <!-- 判断没有子路由的 -->
      <el-menu-item
        v-if="!item.children"
        :key="item.url"
        :index="item.name"
        @click="clickMenu(item)"
      >
        <i :class="`el-icon-${item.icon}`"></i>
        <span slot="title">{{ item.name }}</span>
      </el-menu-item>
      <!-- 有子路由的导航 -->
      <el-submenu v-else :key="item.url" :index="item.name">
        <template slot="title">
          <i :class="`el-icon-${item.icon}`"></i>
          <span slot="title">{{ item.name }}</span>
        </template>
        <!-- 子路由 -->
        <aside-item :menu="item.children"/>
      </el-submenu>
    </template>
  </div>
</template>

<script>

export default {
  name: "AsideItem",
  props: ["menu"],
  methods: {
    clickMenu(item) {
      if (this.$route.path !== item.url) {
        //如果当前路由跟需要更新的路由不一样
        this.$router.push(item.url);
      }
    },
  },
};
</script>
<style lang="less">
.el-menu--collapse .el-menu-item span,
.el-menu--collapse .el-submenu .el-submenu__title span {
  height: 0;
  width: 0;
  overflow: hidden;
  visibility: hidden;
  display: inline-block;
}
</style>

<template>
  <div :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? `#304156` : `#ffffff` }" class="asideContainer">
    <!--    <h3>{{ isCollapse ? "科研" : "科研项目管理系统" }}</h3>-->
    <el-menu
      :background-color="settings.sideTheme === 'theme-dark' ? `#304156` : `#ffffff`"
      :collapse="isCollapse"
      :collapse-transition="false"
      :text-color="settings.sideTheme === 'theme-dark' ? `#bfcbd9` : `rgba(0,0,0,.70)`"
      :unique-opened="true"
      active-text-color="#ffd04b"
      class="el-menu-vertical-demo"
      default-active="1-4-1"
    >
      <AsideItems :menu="getMenuList"/>
    </el-menu>
  </div>
</template>

<script>
import {mapGetters, mapState} from "vuex";
import AsideItems from "./AsideItems"

export default {
  name: "Aside",
  components: {AsideItems},
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["isCollapse"]),
    getMenuList() {
      return this.$store.getters.menuList;
    },
  },
};
</script>

<style lang="less" scoped>
.el-menu {
  min-height: 200vh;
  height: 300%;
  border-right: none;

  h3 {
    color: white;
    text-align: center;
    line-height: 48px;
    font-size: 16px;
    font-weight: 400;
    padding: 0 20px;
  }
}
</style>

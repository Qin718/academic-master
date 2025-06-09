<template>
  <div :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? `#304156` : `#ffffff` }" class="asideContainer">
    <Logo v-if="showLogo" :collapse="isCollapse"/>
    <!--    <h3>{{ isCollapse ? "科研" : "科研项目管理系统" }}</h3>-->
    <AsideMenu v-if="!showTopNav"/>
    <sidebarMenu v-if="showTopNav"/>
  </div>
</template>
<script>
import {mapGetters, mapState} from "vuex";
import Logo from "./Logo";
import AsideMenu from "./Aside"
import sidebarMenu from "./Sidebar"

export default {
  name: "Aside",
  components: {Logo, AsideMenu, sidebarMenu},
  data() {
    return {};
  },
  methods: {},
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["isCollapse"]),
    //是否显示logo
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    //是否显示顶部导航
    showTopNav() {
      return this.$store.state.settings.topNav;
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

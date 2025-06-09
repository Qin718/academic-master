<template>
  <div :style="{ backgroundColor: settings.sideTheme === 'theme-dark' ? `#304156` : `#ffffff` }" class="asideContainer">
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

      <el-menu-item
        v-for="item in sidebarMenu"
        :key="item.url"
        :index="item.name"
        @click="clickMenu(item)"
      >
        <i :class="`el-icon-${item.icon}`"></i>
        <span slot="title">{{ item.name }}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import {mapGetters, mapState} from "vuex";

export default {
  name: "Sidebar",
  data() {
    return {
      menuData: [],
    };
  },
  methods: {
    clickMenu(item) {
      if (this.$route.path !== item.url) {
        //如果当前路由跟需要更新的路由不一样
        this.$router.push(item.url);
      }
    },
  },
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["sidebarMenu","isCollapse"]),
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

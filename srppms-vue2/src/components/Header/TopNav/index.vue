<template>
  <el-menu mode="horizontal" @select="handleSelect">
    <template v-for="item in topMenus">
      <el-menu-item v-if="item['orderNum'] <= visibleNumber" :key="item.id" :index="String(item.id)"
                    :style="{'--theme': theme}">
        <i :class="`el-icon-${item.icon}`"/>
        {{ item.name }}
      </el-menu-item>
    </template>
    <!-- 顶部菜单超出数量折叠 -->
    <el-submenu v-if="topMenus.length > visibleNumber" :style="{'--theme': theme}" index="">
      <template slot="title">更多菜单</template>
      <template v-for="item in topMenus">
        <el-menu-item v-if="item['orderNum'] > visibleNumber" :key="item.id" :index="String(item.id)">
          <i :class="`el-icon-${item.icon}`"/>
          {{ item.name }}
        </el-menu-item>
      </template>
    </el-submenu>
  </el-menu>
</template>

<script>

import {mapGetters} from "vuex";

export default {
  computed: {
    ...mapGetters(["menuList"]),
    theme() {
      return this.$store.state.settings.theme;
    },
  },
  data() {
    return {
      // 顶部栏初始数
      visibleNumber: 5,
      topMenus: [],
    };
  },
  methods: {
    // 根据宽度计算设置显示栏数
    setVisibleNumber() {
      const width = document.body.getBoundingClientRect().width / 3;
      this.visibleNumber = parseInt(width / 85);
    },
    // 顶部显示菜单
    getMenus() {
      this.topMenus = this.menuList
      this.handleSelect("2")
    },
    // 菜单选择事件
    handleSelect(id) {
      const menu = this.topMenus.filter(item => String(item.id) === id)[0]
      if (menu.children !== null) {
        this.$store.commit("setSidebarMenu", menu.children);
      } else {
        this.$router.push(menu.url)
      }
    },
  },
  mounted() {
    this.setVisibleNumber();
    this.getMenus();
  },
  beforeMount() {
    window.addEventListener('resize', this.setVisibleNumber)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.setVisibleNumber)
  },
};
</script>

<style lang="less">
</style>

<template>
  <div :style="{ backgroundColor: sideTheme === 'theme-dark' ? `#304156` : `#ffffff` }"
       class="container">
    <transition>
      <router-link class="link" to="/Index">
        <img :src="logo" alt="" class="logo">
        <!-- logo 文字 -->
        <h1 v-if="!isCollapse" :style="{ color: sideTheme === 'theme-dark' ? `#ffffff` : `#001529` }"
            class="title">
          {{ title }}
        </h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'
import {title} from "@/settings";
import {mapGetters} from "vuex";

export default {
  name: 'logo',
  data() {
    return {
      title: title,
      logo: logoImg
    }
  },
  computed: {
    ...mapGetters(["isCollapse"]),
    sideTheme() {
      return this.$store.state.settings.sideTheme
    },
  }
}
</script>

<style lang="less" scoped>
.container {
  position: relative;
  width: 100%;
  height: 50px;
  line-height: 50px;
  text-align: center;
  overflow: hidden;

  & .link {
    height: 100%;
    width: 100%;

    & .logo {
      width: 32px;
      height: 32px;
      vertical-align: middle;
      margin-right: 12px;
    }

    & .title {
      display: inline-block;
      margin: 0;
      font-weight: 600;
      line-height: 50px;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }
  }
}
</style>

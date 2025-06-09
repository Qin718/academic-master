<template>
  <div class="header-container">
    <div class="l-content">
      <collapse/>
      <hamburger v-if="!topNav"/>
      <top-nav v-if="topNav"/>
    </div>
    <div class="r-content" style="display: flex">
      <user/>
    </div>
  </div>
</template>
<script>
import collapse from "./collapse"
import hamburger from "./hamburger"
import user from "./user"
import topNav from "./TopNav"

export default {
  name: "Header",
  components: {
    collapse,
    hamburger,
    user,
    topNav
  },
  computed: {
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  }
};
</script>
<style lang="less">
.header-container {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .r-content {
    padding-right: 30px;

    .user_img {
      width: 40px;
      height: 40px;
      border-radius: 50%;
    }
  }

  .l-content {
    display: flex;
    align-items: center;
  }
}
</style>

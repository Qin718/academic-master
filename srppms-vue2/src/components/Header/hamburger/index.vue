<template>
  <div class="header-container">
    <div class="l-content">
      <el-breadcrumb separator=">" style="padding-left: 20px">
        <transition-group>
          <!-- 面包屑 -->
          <el-breadcrumb-item
            v-for="item in topTags"
            :key="item.name"
            :effect="$route.path === item.url ? 'dark' : 'plain'"
          >
            <!--            @click.native="changeMenu(item)"-->
            {{ item.name }}
          </el-breadcrumb-item>
        </transition-group>
      </el-breadcrumb>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";

//头部面包屑
export default {
  name: "crumbs",
  methods: {},
  computed: {
    ...mapGetters(["topTags"])
  },
};
</script>
<style lang="less">
.v-enter-active, .v-leave-active {
  transition: all 0.5s;
}
.v-enter, .v-leave-active {
  opacity: 0;
  transform: translateX(20px);
}
.v-leave-active {
  position: absolute;
}
</style>

<template>
  <div class="tabs">
    <div v-horizontal-scroll class="tabs-container" style="margin-left: 1%">
      <el-tag
        v-for="(item) in commonTags"
        :key="item.url"
        :closable="item.name !== '首页'"
        :effect="$route.path === item.url ? 'dark' : 'plain'"
        @click="changeMenu(item)"
        @close="closeMenu(item)"
      >
        {{ item.name }}
      </el-tag>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import store from "@/store";

export default {
  name: "commonTab",
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["commonTags"])
  },
  methods: {
    changeMenu(item) {
      this.$router.push(item.url);
    },
    closeMenu(item) {
      //关闭菜单
      store.dispatch('app/closeMenu',item)
      //动态的处理路由，关闭当前页面，定位到首页
      if (this.$route.path !== item.url) {
        return;
      }
      this.$router.push("/Index");
    },
  },
  //实现滚动条
  directives: {
    "horizontal-scroll": {
      bind: function (el) {
        el.addEventListener("wheel", function (event) {
          event.preventDefault();
          el.scrollLeft = el.scrollLeft + event.deltaY;
        });
      },
    },
  },
};
</script>
<style lang="less" scoped>
.tabs {
  position: relative;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 5px 10px #ddd;
  padding-bottom: 5px;
  padding-top: 5px;
  padding-left: 10px;

  .el-tag {
    margin-right: 15px;
    cursor: pointer;
  }
}

.tabs-container {
  width: 100%;
  height: 100%;
  overflow-x: scroll;
  overflow-y: hidden;
  white-space: nowrap;
  display: flex;
}

.tabs-container::-webkit-scrollbar {
  display: none;
}
</style>

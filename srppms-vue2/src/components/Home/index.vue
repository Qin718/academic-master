<template>
  <div class="hello">
    <el-container>
      <el-aside :style="{ width : isCollapse ? `auto` : `10%`}">
        <!-- 左侧菜单 -->
        <Aside/>
      </el-aside>
      <el-container>
        <el-header>
          <!-- 头部 -->
          <Header/>
        </el-header>
        <!-- 方形面包屑 -->
        <common-tab v-if="needTagsView"/>
        <el-main>
          <div class="loading-area" style="width: 100%;height: 100%">
            <transition mode="out-in" name="fade-transform">
            <!-- 路由出口 -->
            <router-view/>
            </transition>
          </div>
          <!-- 右侧面板 -->
          <right-panel/>
          <!-- 回到顶部图标 -->
          <Top/>
          <!-- Websocket -->
          <Socket/>
        </el-main>
      </el-container>
    </el-container>
    <el-footer>
      <!-- 底部 -->
      <Footer/>
    </el-footer>
  </div>
</template>

<script>
import Aside from "../Aside";
import Header from "../Header";
import CommonTab from "../CommonTab";
import Socket from "../WebSocket";
import Top from "../Top";
import Footer from "../Footer";
import RightPanel from "../utils/RightPanel";
import {mapGetters} from "vuex";
import ResizeMixin from "@/method/ResizeHandler"
import {message} from "@/method/message";

export default {
  name: "Home",
  data(){
    return{
      onLine: navigator.onLine
    }
  },
  components: {
    Aside,
    Header,
    CommonTab,
    Socket,
    Top,
    Footer,
    RightPanel,
  },
  methods: {
    //全局参数
    setGlobalConfig() {
      this.$http.post("/config/getMap").then((res) => {
        this.$setGlobalConfig(res)
      });
    },
    //检查网络
    checkOnline(online){
      if (online){
        this.$GlobalLoading(false,{})
      }else {
        this.$GlobalLoading(true,{text:"网络正在加载中,请稍后...."})
        setTimeout(()=>{
          this.$GlobalLoading(false,{})
          message.info("网络加载失败，请检查网络！");
          this.onLine = true;
        },3000)
      }
      window.addEventListener('offline', () => {
        this.checkOnline(navigator.onLine);
      })
      window.addEventListener('online', () => {
        message.success("网络连接成功");
        this.checkOnline(navigator.onLine);
      })
    },
  },
  mounted() {
    this.setGlobalConfig();
    this.checkOnline(this.onLine);
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapGetters(["isCollapse","needTagsView"]),
  },
  beforeDestroy() {
    window.removeEventListener('online', this.checkOnline);
    window.removeEventListener('offline', this.checkOnline);
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less" scoped>
/** 去除左侧菜单滚动条 */
.el-aside::-webkit-scrollbar {
  display: none;
}

/** 去除el-main滚动条 */
.el-main::-webkit-scrollbar {
  display: none;
}

/* fade-transform */
.fade-transform--move,
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all .5s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

</style>

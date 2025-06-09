<template>
	<div>

	</div>
</template>

<script>
import Axios from "axios";
import {message} from "@/method/message";
import {getRouter} from "@/router";
import cache from "@/method/cache";

export default {
  mounted() {
    //初始化websocket
	  this.initWebSocket();
  },
  destroyed: function () {
    // 离开页面生命周期函数
    this.websocketClose();
  },
  methods: {
    initWebSocket: function () { // 建立连接
      // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
	    const Id = cache.session.get("account");
      const url = Axios.defaults.baseURL.replace("https://", "wss://").replace("http://", "ws://") + "/WebSocket/" + Id;
      this.websock = new WebSocket(url);
      this.websock.onopen = this.websocketOpen;
      this.websock.send = this.websocketSend;
      this.websock.onerror = this.websocketError;
      this.websock.onmessage = this.websocketMessage;
      this.websock.onclose = this.websocketClose;
    },
    // 连接成功后调用
    websocketOpen: function () {
      // console.log("WebSocket连接成功");
    },
    // 发生错误时调用
    websocketError: function (e) {
      // console.log("WebSocket连接发生错误", e);
    },
    // 给后端发消息时调用
    websocketSend: function (e) {
      // console.log("给后端发消息时发生错误", e);
    },
    // 接收后端消息
    // vue 客户端根据返回的type类型处理不同的业务响应
    websocketMessage: function (res) {
      res = res.data
      const json_res = JSON.parse(res)
      //message弹窗
      if (json_res.message !== null && json_res.message !== undefined) {
        message({type: "success", message: json_res.message})
      }
      //系统公告
      if (json_res.type === 'notify') {
        const data = json_res.data
        const options = {}
        //标题
        if (data.title !== "") {
          options["title"] = data.title
        }
        //内容
        if (data.message !== "") {
          let style = {}
          //颜色
          if (data.color !== "") {
            style = {style: ' color: ' + data.color}
          }
          options["message"] = this.$createElement('i', style, data.message)
        }
        //存在时间
        if (data.duration !== "") {
          options["duration"] = data.duration
        }
        //图标
        if (data.icon !== "") {
          options["iconClass"] = "el-icon-" + data.icon
        }
        this.$notify(options);
      }
      //强制退出
      if (json_res.type === 'forcedOut') {
        // 清空存储的Token信息
	      cache.session.removeAll();
        //清除自动登录
	      cache.local.remove("auto");
        // 然后跳转到登录页
        this.$router.push("/");
      }
      //用户信息
      if (json_res.type === 'userInfo') {
        this.$store.dispatch('userInfo/setUserInfo', json_res.data);
        //刷新路由菜单
        getRouter()
      }
    },
    // 关闭连接时调用
    websocketClose: function (e) {
      // console.log("connection closed (" + e + ")");
    },
  }
}
</script>

<style scoped>

</style>

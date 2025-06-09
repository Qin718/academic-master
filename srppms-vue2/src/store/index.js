import Vue from "vue";
import Vuex from "vuex";
import settings from "@/method/settings"
import getters from "@/method/getters";
import permission from "@/method/permission";
import userInfo from "@/method/userInfo";
import app from "@/method/app";

Vue.use(Vuex)
const store = new Vuex.Store({
  modules: {
    settings,
    permission,
    userInfo,
    app,
  },
  getters
})
export default store

import defaultSettings from '@/settings'
import cache from "@/method/cache";

const {sideTheme, showSettings, topNav, tagsView, sidebarLogo, dynamicTitle} = defaultSettings

const storageSetting = JSON.parse(cache.local.get('ThemeSetting')) || ''
const state = {
  title: '',
  theme: storageSetting.theme || '#409EFF',
  sideTheme: storageSetting.sideTheme || sideTheme,
  showSettings: showSettings,
  topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
  tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
  sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
  dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle
}
const mutations = {
  changeSetting: (state, {key, value}) => {
    state[key] = value
  }
}

const actions = {
  // 修改布局设置
  changeSetting({commit}, data) {
    commit('changeSetting', data)
  },
  // 设置网页标题
  setTitle({commit}, title) {
    state.title = title
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}


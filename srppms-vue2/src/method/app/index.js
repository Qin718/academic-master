const state = {
  //菜单栏是否关闭
  isCollapse: false,
  device: 'desktop',
  //头部面包屑数据
  headerTabsList: [{url: '/Index', name: '首页'}],
  //公共面包屑数据
  commonTabsList: [{url: '/Index', name: '首页'}],
}

const mutations = {
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  CHANGE_COLLAPSE(state) {
    state.isCollapse = !state.isCollapse
  },
  ROUTER_CHANGE(state, to) {
    //防止空tab和登录
    if (to["name"] === null || to["path"] === "/") return;

    const data = {
      name: to["meta"]["title"],
      url: to["path"],
      parentName: to["meta"]['parentName']
    }

    //=====头部面包屑============
    state.headerTabsList = []
    state.headerTabsList.push({url: '/Index', name: '首页'})
    const menuName = data.name
    state.headerTabsList = state.headerTabsList.filter(item => item.name !== menuName)
    //父菜单
    const parentName = data.parentName
    if (parentName !== null && parentName !== undefined) {
      state.headerTabsList.push({url: data.url, name: parentName})
    }
    //此菜单
    const menu = {url: data.url, name: menuName};
    state.headerTabsList.push(menu)

    //=====公共面包屑============
    //判断数据是否存在
    const index = state.commonTabsList.findIndex(item => item.name === menuName)
    if (index === -1) {
      state.commonTabsList.push(menu)
    }
  },
  CLOSE_MENU(state,data){
    state.headerTabsList = state.headerTabsList.filter(item => item.name !== data.name)
    state.commonTabsList = state.commonTabsList.filter(item => item.name !== data.name)
  }
}

const actions = {
  toggleDevice({commit}, device) {
    commit('TOGGLE_DEVICE', device)
  },
  changeCollapse({commit}){
    commit('CHANGE_COLLAPSE')
  },
  routerChange({commit}, to) {
    commit('ROUTER_CHANGE', to)
  },
  closeMenu({commit},data){
    commit('CLOSE_MENU',data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

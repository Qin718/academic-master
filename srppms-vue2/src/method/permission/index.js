const permission = {
  state: {
    menuList: [],
    sidebarMenu: [],
  },
  mutations: {
    setMenuList: (state, menu) => {
      if(menu !== undefined) {
        state.menuList = menu.filter(menu => menu.isShow !== "不显示")
      }
    },
    setSidebarMenu: (state, menu) => {
      state.sidebarMenu = menu
    },
  },
}

export default permission

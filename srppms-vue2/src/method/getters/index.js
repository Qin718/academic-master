const getters = {
  isCollapse: state => state.app.isCollapse,
  menuList: state => state.permission.menuList,
  sidebarMenu: state => state.permission.sidebarMenu,
  userInfo: state => state.userInfo,
  needTagsView: state => state.settings.tagsView,
  topTags: state => state.app.headerTabsList,
  commonTags: state => state.app.commonTabsList,
}
export default getters

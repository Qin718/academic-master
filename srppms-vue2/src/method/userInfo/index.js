const state = {
  image: "",
  roleId: 0,
  ip: "",
  roleName: "",
  username: "",
  time: "",
  mobile: "",
  email: "",
  createTime: "",
  sex: ""
}
const mutations = {
  changeUserInfo: (state, data) => {
    for (let k in data){
      state[k] = data[k];
    }
  }
}
const actions = {
  setUserInfo({commit}, data) {
    commit('changeUserInfo', data)
  }
}
export default {
  namespaced: true,
  state,
  mutations,
  actions,
}

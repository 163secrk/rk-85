import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: {
      name: '管理员',
      role: '经纪人'
    },
    sidebarCollapsed: false
  },
  mutations: {
    TOGGLE_SIDEBAR(state) {
      state.sidebarCollapsed = !state.sidebarCollapsed
    },
    SET_USER(state, user) {
      state.user = user
    }
  },
  actions: {
    toggleSidebar({ commit }) {
      commit('TOGGLE_SIDEBAR')
    },
    setUser({ commit }, user) {
      commit('SET_USER', user)
    }
  },
  getters: {
    sidebarCollapsed: state => state.sidebarCollapsed,
    user: state => state.user
  }
})

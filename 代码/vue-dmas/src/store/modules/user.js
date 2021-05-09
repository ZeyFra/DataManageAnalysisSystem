import { login, logout, getInfo, register } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    userName: '',
    realNname: '',
    avatar: '',
    qq: '',
    gender: '',
    telephone: '',
    userId: '',
    roles: []
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_REALNAME: (state, realName) => {
    state.realName = realName
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_GENDER: (state, gender) => {
    state.gender = gender
  },
  SET_QQ: (state, qq) => {
    state.qq = qq
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_TELEPHONE: (state, telephone) => {
    state.telephone = telephone
  },
  SET_USERID: (state, userId) => {
    state.userId = userId
  },
  SET_USERNAME: (state, userName) => {
    state.userName = userName
  }
}

const actions = {

  login({ commit }, userInfo) {
    const { userName, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ userName: userName.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo().then(response => {
        const { data } = response
        if (!data) {
          return reject('校验失败，请重新登录！')
        }
        const avatar = 'https://images.cnblogs.com/cnblogs_com/zeyfra/1860521/o_201009150320Cache_46c7804f0c6bbd5..jpg'
        const { realName, telephone, qq, gender, roles, userId, userName } = data
        commit('SET_REALNAME', realName)
        commit('SET_USERNAME', userName)
        commit('SET_AVATAR', avatar)
        commit('SET_QQ', qq)
        commit('SET_TELEPHONE', telephone)
        commit('SET_GENDER', gender)
        commit('SET_ROLES', roles)
        commit('SET_USERID', userId)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_REALNAME', '')
        commit('SET_AVATAR', '')
        commit('SET_QQ', '')
        commit('SET_TELEPHONE', '')
        commit('SET_GENDER', '')
        commit('SET_USERNAME', '')
        commit('SET_USERID', '')
        commit('SET_ROLES', [])
        removeToken() // must remove  token  first
        resetRouter()
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  register({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      register(userInfo).then(response => {
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_REALNAME', '')
      commit('SET_AVATAR', '')
      commit('SET_QQ', '')
      commit('SET_TELEPHONE', '')
      commit('SET_GENDER', '')
      commit('SET_USERNAME', '')
      commit('SET_USERID', '')
      commit('SET_ROLES', [])
      removeToken() // must remove  token  first
      // commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}


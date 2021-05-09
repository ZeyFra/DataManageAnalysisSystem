import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 10000
})

// request interceptor
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// 相应拦截
service.interceptors.response.use(

  response => {
    const res = response.data
    // 状态码不为200则登录失败
    if (res.code !== 200) {
      Message({
        message: res.message || '错误！',
        type: 'error',
        duration: 1.5 * 1000,
        showClose: true
      })

      // 登录超时响应码
      if (res.code === 10010002) {
        // to re-login
        MessageBox.confirm('登录超时，请重新登录！', '登录提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning',
          duration: 1.5 * 1000,
          showClose: true
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.msg || '错误！'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error)
    Message({
      message: error.message,
      type: 'error',
      duration: 1.5 * 1000,
      showClose: true
    })
    return Promise.reject(error)
  }
)

export default service

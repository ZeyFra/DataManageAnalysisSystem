import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/dmas/api/user/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/dmas/api/user/getUserInfo',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/dmas/api/user/logout',
    method: 'post'
  })
}

export function register(data) {
  return request({
    url: '/dmas/api/system/user/register',
    method: 'put',
    data
  })
}

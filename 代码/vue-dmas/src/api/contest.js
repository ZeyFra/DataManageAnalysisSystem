import request from '@/utils/request'

export function getContestList(params) {
  return request({
    url: '/dmas/api/contest/pageContestList',
    method: 'get',
    params
  })
}

export function getContestInfo(params) {
  return request({
    url: '/dmas/api/contest/getContestInfo',
    method: 'get',
    params
  })
}

export function getContestMemberDetailList(params) {
  return request({
    url: '/dmas/api/contest/getContestMemberDetailList',
    method: 'get',
    params
  })
}

export function getContestMemberColumnList(params) {
  return request({
    url: 'dmas/api/contest/getContestMemberColumnList',
    method: 'get',
    params
  })
}

export function queryContest(data) {
  return request({
    url: 'dmas/api/contest/queryContest',
    method: 'post',
    data
  })
}

export function updateContest(data) {
  return request({
    url: 'dmas/api/contest/updateContest',
    method: 'post',
    data
  })
}

export function deleteContest(data) {
  return request({
    url: 'dmas/api/contest/deleteContest',
    method: 'delete',
    data
  })
}

export function getTemplate() {
  return request({
    url: 'dmas/api/contest/getTemplate',
    method: 'get'
  })
}

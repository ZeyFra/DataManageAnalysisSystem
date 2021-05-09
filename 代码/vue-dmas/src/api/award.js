import request from '@/utils/request'

export function getAwardList(params) {
  return request({
    url: '/dmas/api/award/getAwardList',
    method: 'get',
    params
  })
}

export function getAwardRecordByMemberId(params) {
  return request({
    url: '/dmas/api/award/getAwardRecordByMemberId',
    method: 'get',
    params
  })
}

export function getAllRecordDistribution() {
  return request({
    url: '/dmas/api/award/getAllRecordDistribution',
    method: 'get'
  })
}

export function getAllRecordYearDistribution() {
  return request({
    url: '/dmas/api/award/getAllRecordYearDistribution',
    method: 'get'
  })
}
